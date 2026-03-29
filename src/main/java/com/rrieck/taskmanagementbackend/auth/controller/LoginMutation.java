package com.rrieck.taskmanagementbackend.auth.controller;

import com.rrieck.taskmanagementbackend.auth.schema.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.LoginUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LoginMutation {
	private final LoginUserService loginUserService;

	@MutationMapping
	public AuthTypes.AuthResponseType login(@Argument AuthTypes.LoginUserInput input) {
		return loginUserService.login(
			input.email(),
			input.password()
		);
	}
}
