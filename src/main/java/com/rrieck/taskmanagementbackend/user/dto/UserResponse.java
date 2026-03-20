package com.rrieck.taskmanagementbackend.user.dto;

import com.rrieck.taskmanagementbackend.user.model.UserId;
import lombok.Builder;

@Builder
public record UserResponse(
	UserId id,
	String name,
	String email,
	String role
) {
}
