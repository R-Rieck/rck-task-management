package com.rrieck.taskmanagementbackend.auth.service.authentication;

import com.rrieck.taskmanagementbackend.auth.model.AuthorizationContext;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class AuthorizationWrapper {
	private final JwtTokenProvider jwtTokenProvider;

	public static <T> T maybeAuthenticated(Authentication authentication, java.util.function.Function<Optional<AuthorizationContext>, T> processor) {
		if (authentication == null
			|| !authentication.isAuthenticated()
			|| authentication instanceof AnonymousAuthenticationToken
		) {
			return processor.apply(Optional.empty());
		} else {
			return authenticated(
				authentication,
				context -> processor.apply(Optional.of(context))
			);
		}
	}

	public static <T> T authenticated(Authentication authentication, java.util.function.Function<AuthorizationContext, T> processor) {
		if (authentication == null
			|| !authentication.isAuthenticated()
			|| authentication instanceof AnonymousAuthenticationToken
		) {
			throw new AuthenticationCredentialsNotFoundException("Authentication is required");
		}

		AuthorizationContext context;
		try {
			context = (AuthorizationContext) authentication.getPrincipal();
		} catch (ClassCastException exception) {
			throw new AuthenticationCredentialsNotFoundException("Can not extract authorizationContext", exception);
		}


		return processor.apply(context);
	}
}
