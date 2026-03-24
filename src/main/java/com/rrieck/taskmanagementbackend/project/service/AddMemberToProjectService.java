package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.project.dto.ProjectMemberResponse;
import com.rrieck.taskmanagementbackend.project.dto.ProjectWithMemberResponse;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.model.ProjectMember;
import com.rrieck.taskmanagementbackend.project.model.ProjectMemberId;
import com.rrieck.taskmanagementbackend.project.repository.ProjectMemberRepository;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddMemberToProjectService {
	private final ProjectMemberRepository projectMemberRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;

	public ProjectWithMemberResponse add(List<UserId> userId, ProjectId projectId) {
		Project projectRef = projectRepository.getReferenceById(projectId);
		List<User> userRefs = userRepository.getReferenceByIdIsIn(userId);

		List<ProjectMember> projectMembers = userRefs.stream().map((User user) -> ProjectMember
			.builder()
			.id(ProjectMemberId.generateId())
			.project(projectRef)
			.user(user)
			.joinedAt(LocalDateTime.now())
			.build()
		).toList();

		projectMemberRepository.saveAll(projectMembers);

		List<ProjectMemberResponse> projectMemberResponse = projectMembers
			.stream()
			.map(projectMember -> ProjectMemberResponse
				.builder()
				.user(UserResponse.from(projectMember.getUser()))
				.joinedAt(projectMember.getJoinedAt())
				.build())
			.toList();

		return ProjectWithMemberResponse.builder().project(projectRef).member(projectMemberResponse).build();
	}
}
