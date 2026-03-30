package com.rrieck.taskmanagementbackend.auth.schema;

import com.rrieck.taskmanagementbackend.auth.service.LogoutUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LogoutMutation {
	private final LogoutUserService logoutUserService;

	@MutationMapping
	public Boolean logout(@Argument AuthTypes.LogoutInput input) {
		logoutUserService.logout(input.refreshToken());
		return true;
	}
}
