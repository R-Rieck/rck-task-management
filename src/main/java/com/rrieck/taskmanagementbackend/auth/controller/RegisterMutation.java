package com.rrieck.taskmanagementbackend.auth.controller;

import com.rrieck.taskmanagementbackend.auth.schema.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.RegisterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RegisterMutation {
	private final RegisterUserService registerUserService;

	@MutationMapping
	public AuthTypes.AuthResponseType register(@Argument AuthTypes.RegisterUserInput input) {
		return registerUserService.register(
			input.name(),
			input.email(),
			input.password()
		);
	}
}
