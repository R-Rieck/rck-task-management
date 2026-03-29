package com.rrieck.taskmanagementbackend.user.dto;

import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import lombok.Builder;

@Builder
public record UserResponse(
	UserId id,
	String name,
	String email
) {
	public static UserResponse from(User user) {
		return UserResponse
			.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.build();
	}
}
