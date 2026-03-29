package com.rrieck.taskmanagementbackend.auth.controller;

import com.rrieck.taskmanagementbackend.auth.schema.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.RefreshAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RefreshTokenMutation {
	private final RefreshAuthenticationService refreshAuthenticationService;

	@MutationMapping
	public AuthTypes.AuthResponseType refresh(@Argument AuthTypes.RefreshAuthenticationInput input) {
		return refreshAuthenticationService.refresh(input.refreshToken());
	}
}
