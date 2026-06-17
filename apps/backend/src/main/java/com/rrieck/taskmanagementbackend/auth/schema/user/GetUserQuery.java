package com.rrieck.taskmanagementbackend.auth.schema.user;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.auth.service.user.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GetUserQuery {
	private final GetUserByIdService getUserByIdService;

	@QueryMapping
	public UserTypes.UserType me(Authentication authentication) {
		return AuthorizationWrapper.authenticated(authentication, ctx ->
			getUserByIdService.get(ctx.userId())
		);
	}
}
