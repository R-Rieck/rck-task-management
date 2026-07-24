package com.rrieck.taskmanagementbackend.project.schema;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.schema.user.UserTypes;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMember;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMemberId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectTypes {
	@Builder
	public record ProjectMemberType(
		ProjectMemberId id,
		UserTypes.UserType user,
		LocalDateTime joinedAt
	) {
		public static ProjectMemberType from(ProjectMember member) {
			return ProjectMemberType.builder()
				.id(member.getId())
				.user(UserTypes.UserType.from(member.getUser()))
				.joinedAt(member.getJoinedAt())
				.build();
		}
	}

	@Builder
	public record ProjectType(
		ProjectId id,
		String name,
		String description,
		String icon,
		UserId ownerId,
		List<ProjectMemberType> members,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		public static ProjectType from(Project project) {
			return ProjectType.builder()
				.id(project.getId())
				.name(project.getName())
				.description(project.getDescription())
				.icon(project.getIcon())
				.ownerId(project.getOwner().getId())
				.members(project.getMembers().stream().map(ProjectMemberType::from).toList())
				.createdAt(project.getCreatedAt())
				.updatedAt(project.getUpdatedAt())
				.build();
		}
	}

	public record CreateProjectInput(
		String name,
		String description,
		String icon,
		List<UserId> memberIds
	) {}

	public record EditProjectInput(
		ProjectId projectId,
		String name,
		String description,
		String icon,
		List<UserId> memberIds
	) {}
}
