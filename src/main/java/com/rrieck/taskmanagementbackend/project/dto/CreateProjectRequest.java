package com.rrieck.taskmanagementbackend.project.dto;

import java.util.Optional;
import java.util.UUID;


public record CreateProjectRequest(
	String name,
	Optional<String> description,
	UUID owner
) {
}
