package com.rrieck.taskmanagementbackend.common.security;

import com.rrieck.taskmanagementbackend.user.model.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticatedUserTest {
	@Test
	void requireUserIdReturnsAuthenticatedUserId() {
		UserId userId = UserId.generateId();
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				userId.id().toString(),
				null,
				AuthorityUtils.NO_AUTHORITIES
			);

		assertEquals(userId.id(), AuthenticatedUser.requireUserId(authentication).id());
	}

	@Test
	void requireUserIdRejectsMissingAuthentication() {
		assertThrows(
			AuthenticationCredentialsNotFoundException.class,
			() -> AuthenticatedUser.requireUserId(null)
		);
	}

	@Test
	void requireUserIdRejectsAnonymousAuthentication() {
		AnonymousAuthenticationToken authentication =
			new AnonymousAuthenticationToken(
				"anonymous",
				"anonymousUser",
				AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
			);

		assertThrows(
			AuthenticationCredentialsNotFoundException.class,
			() -> AuthenticatedUser.requireUserId(authentication)
		);
	}
}
