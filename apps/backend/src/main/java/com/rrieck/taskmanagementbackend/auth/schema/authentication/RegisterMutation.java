package com.rrieck.taskmanagementbackend.auth.schema.authentication;

import com.rrieck.taskmanagementbackend.auth.service.authentication.RegisterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RegisterMutation {
	private final RegisterUserService registerUserService;

	@MutationMapping
	public AuthTypes.AuthType register(@Argument AuthTypes.RegisterUserInput input) {
		return registerUserService.register(
			input.name(),
			input.email(),
			input.password()
		);
	}
}
