package com.rrieck.taskmanagementbackend.project.schema;

import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import lombok.Builder;

import java.time.LocalDateTime;

public class ProjectTypes {
	@Builder
	public record ProjectType(
		ProjectId id,
		String name,
		String description,
		boolean isPrivate,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		public static ProjectType from(Project project) {
			return ProjectType.builder()
			                  .id(project.getId())
			                  .name(project.getName())
			                  .description(project.getDescription())
			                  .isPrivate(project.isPrivate())
			                  .createdAt(project.getCreatedAt())
			                  .updatedAt(project.getUpdatedAt())
			                  .build();
		}
	}

	public record CreateProjectInput(
		String name,
		String description,
		Boolean isPrivate
	) {}

	public record EditProjectInput(
		ProjectId projectId,
		String name,
		String description,
		boolean isPrivate
	) {}
}
