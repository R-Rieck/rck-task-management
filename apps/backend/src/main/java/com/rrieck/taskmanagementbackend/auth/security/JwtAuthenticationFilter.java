package com.rrieck.taskmanagementbackend.auth.security;

import com.rrieck.taskmanagementbackend.auth.model.AuthorizationContext;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.accessToken.CheckAccessTokenForValidity;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.token.JwtProperties;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.token.JwtTokenProvider;
import com.rrieck.taskmanagementbackend.auth.service.authentication.userDetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final CustomUserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtProperties jwtProperties;
	private final CheckAccessTokenForValidity checkAccessTokenForValidity;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);
		String secret = jwtProperties.getAccessToken().getSecret();
		String email;
		UserId userId;
		AccountId accountId;

		try {
			email = jwtTokenProvider.extractEmail(token, secret);
			userId = jwtTokenProvider.extractUserId(token, secret);
			accountId = jwtTokenProvider.extractAccountId(token, secret);
		} catch (Exception ex) {
			filterChain.doFilter(request, response);
			return;
		}

		if (SecurityContextHolder.getContext().getAuthentication() == null
			&& checkAccessTokenForValidity.isValid(token, email)) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);
			AuthorizationContext authContext = AuthorizationContext
				.builder()
				.userId(userId)
				.accountId(accountId)
				.email(email)
				.build();

			UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(
					authContext,
					null,
					userDetails.getAuthorities()
				);

			authToken.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
			);

			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		filterChain.doFilter(request, response);
	}
}
