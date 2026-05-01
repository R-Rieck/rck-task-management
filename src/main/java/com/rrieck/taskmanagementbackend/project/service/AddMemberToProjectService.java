package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMember;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMemberId;
import com.rrieck.taskmanagementbackend.project.repository.ProjectMemberRepository;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
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

	public void add(List<UserId> userId, ProjectId projectId) {
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
	}
}
