package com.rrieck.taskmanagementbackend.common.security;

import com.rrieck.taskmanagementbackend.user.model.UserId;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;

public final class AuthenticatedUser {
	private AuthenticatedUser() {
	}

	public static UserId requireUserId(Authentication authentication) {
		if (
			authentication == null
				|| !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken
		) {
			throw new AuthenticationCredentialsNotFoundException("Authentication is required");
		}

		try {
			return UserId.fromString(authentication.getName());
		} catch (IllegalArgumentException exception) {
			throw new AuthenticationCredentialsNotFoundException("Authentication is required", exception);
		}
	}
}
