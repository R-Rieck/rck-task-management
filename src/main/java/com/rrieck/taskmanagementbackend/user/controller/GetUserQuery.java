package com.rrieck.taskmanagementbackend.user.controller;

import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import com.rrieck.taskmanagementbackend.user.model.UserId;
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
	public UserResponse me(Authentication authentication) {
		return getUserByIdService.get(UserId.fromString(authentication.getName()));
	}


}
