package com.rrieck.taskmanagementbackend.auth.security;

import com.rrieck.taskmanagementbackend.auth.service.jwt.accessToken.CheckAccessTokenForValidity;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtProperties;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtTokenProvider;
import com.rrieck.taskmanagementbackend.auth.service.userDetails.CustomUserDetailsService;
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
		String userId;

		try {
			userId = jwtTokenProvider
				.extractClaims(token, jwtProperties.getAccessToken().getSecret())
				.getSubject();
		} catch (Exception ex) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserById(userId);

			if (checkAccessTokenForValidity.isValid(token, userId)) {
				UsernamePasswordAuthenticationToken authToken =
					new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities()
					);

				authToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request)
				);

				SecurityContextHolder.getContext().setAuthentication(authToken);
				filterChain.doFilter(request, response);
				return;
			}
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return;
	}
}
