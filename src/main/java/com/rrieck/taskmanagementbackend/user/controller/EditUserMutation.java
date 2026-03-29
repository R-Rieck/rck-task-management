package com.rrieck.taskmanagementbackend.user.controller;

import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.schema.UserTypes;
import com.rrieck.taskmanagementbackend.user.service.EditUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EditUserMutation {
	private final EditUserService editUserService;

	@MutationMapping
	public UserResponse editUser(@Argument UserTypes.EditUserInput input, Authentication authentication) {
		UserId userId = UserId.fromString(authentication.getName());

		return editUserService.edit(
			userId,
			input.name(),
			input.email(),
			input.password()
		);
	}
}
