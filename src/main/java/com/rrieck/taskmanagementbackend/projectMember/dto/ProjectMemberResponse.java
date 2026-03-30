package com.rrieck.taskmanagementbackend.projectMember.dto;

import com.rrieck.taskmanagementbackend.projectMember.model.ProjectMember;
import com.rrieck.taskmanagementbackend.user.schema.UserTypes;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectMemberResponse(
	UserTypes.UserResponse user,
	LocalDateTime joinedAt
) {
	public static ProjectMemberResponse from(ProjectMember projectMember) {
		return ProjectMemberResponse
			.builder()
			.user(UserTypes.UserResponse.from(projectMember.getUser()))
			.joinedAt(projectMember.getJoinedAt())
			.build();
	}
}
