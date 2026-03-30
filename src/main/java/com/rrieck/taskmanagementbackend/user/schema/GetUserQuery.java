package com.rrieck.taskmanagementbackend.user.schema;

import com.rrieck.taskmanagementbackend.common.security.AuthenticatedUser;
import com.rrieck.taskmanagementbackend.user.service.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GetUserQuery {
	private final GetUserByIdService getUserByIdService;

	@QueryMapping
	public UserTypes.UserResponse me(Authentication authentication) {
		return getUserByIdService.get(AuthenticatedUser.requireUserId(authentication));
	}
}
