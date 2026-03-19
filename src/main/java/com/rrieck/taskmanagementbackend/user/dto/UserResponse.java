package com.rrieck.taskmanagementbackend.user.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
	UUID id,
	String name,
	String email,
	String role
) {
}
