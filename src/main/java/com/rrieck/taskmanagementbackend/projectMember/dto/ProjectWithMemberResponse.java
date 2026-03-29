package com.rrieck.taskmanagementbackend.projectMember.dto;

import com.rrieck.taskmanagementbackend.project.dto.ProjectResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectWithMemberResponse(
	ProjectResponse project,
	List<ProjectMemberResponse> member
) {
	public static ProjectWithMemberResponse from(ProjectResponse project, List<ProjectMemberResponse> member) {
		return ProjectWithMemberResponse
			.builder()
			.project(project)
			.member(member)
			.build();
	}
}
