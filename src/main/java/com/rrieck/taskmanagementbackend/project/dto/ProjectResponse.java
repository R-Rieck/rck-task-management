package com.rrieck.taskmanagementbackend.project.dto;

import com.rrieck.taskmanagementbackend.user.model.User;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder
public record ProjectResponse(
	UUID id,
	String name,
	Optional<String> description,
	User owner
) {
}
