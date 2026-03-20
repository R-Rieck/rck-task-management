package com.rrieck.taskmanagementbackend.project.dto;

import com.rrieck.taskmanagementbackend.user.model.UserId;

import java.util.Optional;

public record EditProjectRequest(
	String newName,
	Optional<String> newDescription,
	UserId newOwner
) {
}
