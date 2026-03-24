package com.rrieck.taskmanagementbackend.project.dto;

import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.user.model.User;
import lombok.Builder;

import java.util.Optional;

@Builder
public record ProjectResponse(
	ProjectId id,
	String name,
	Optional<String> description,
	User owner
) {
	public static ProjectResponse from(Project project) {
		return ProjectResponse
			.builder()
			.id(project.getId())
			.name(project.getName())
			.description(Optional.of(project.getDescription()))
			.owner(project.getOwner())
			.build();
	}
}
