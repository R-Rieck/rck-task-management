package com.rrieck.taskmanagementbackend.project.dto;

import java.util.Optional;
import java.util.UUID;

public record EditProjectRequest(
	String newName,
	Optional<String> newDescription,
	UUID newOwner
) {
}
