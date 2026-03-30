package com.rrieck.taskmanagementbackend.user.schema;

import com.rrieck.taskmanagementbackend.auth.service.AuthorizationWrapper;
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
	public UserTypes.UserResponse editUser(@Argument UserTypes.EditUserInput input, Authentication authentication) {
		return AuthorizationWrapper.authenticated(authentication, ctx ->
			editUserService.edit(
				ctx.userId(),
				input.name(),
				input.email(),
				input.password()
			)
		);
	}
}
