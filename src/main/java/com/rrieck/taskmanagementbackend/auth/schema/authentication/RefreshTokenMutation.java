package com.rrieck.taskmanagementbackend.auth.schema.authentication;

import com.rrieck.taskmanagementbackend.auth.service.authentication.RefreshAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RefreshTokenMutation {
	private final RefreshAuthenticationService refreshAuthenticationService;

	@MutationMapping
	public AuthTypes.AuthType refresh(@Argument AuthTypes.RefreshAuthenticationInput input) {
		return refreshAuthenticationService.refresh(input.refreshToken());
	}
}
