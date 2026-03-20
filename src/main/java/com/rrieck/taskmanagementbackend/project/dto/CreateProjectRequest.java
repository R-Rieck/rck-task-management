package com.rrieck.taskmanagementbackend.project.dto;

import com.rrieck.taskmanagementbackend.user.model.UserId;

import java.util.Optional;


public record CreateProjectRequest(
	String name,
	Optional<String> description,
	UserId owner
) {
}
