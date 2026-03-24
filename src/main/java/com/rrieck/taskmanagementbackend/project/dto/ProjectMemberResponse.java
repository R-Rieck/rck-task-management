package com.rrieck.taskmanagementbackend.project.dto;

import com.rrieck.taskmanagementbackend.project.model.ProjectMember;
import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectMemberResponse(
	UserResponse user,
	LocalDateTime joinedAt
) {
	public static ProjectMemberResponse from(ProjectMember projectMember) {
		return ProjectMemberResponse
			.builder()
			.user(UserResponse.from(projectMember.getUser()))
			.joinedAt(projectMember.getJoinedAt())
			.build();
	}
}
