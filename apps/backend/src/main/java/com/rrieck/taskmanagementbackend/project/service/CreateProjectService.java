package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMember;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMemberId;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	@Transactional
	public Project create(String name, String description, String icon, AccountId accountId, UserId ownerUserId, List<UserId> memberIds) {
		User ownerRef = userRepository.getReferenceById(ownerUserId);
		Project project = Project
			.builder()
			.id(ProjectId.generateId())
			.name(name)
			.description(description)
			.icon(icon)
			.account(accountId)
			.owner(ownerRef)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		final Project savedProject = projectRepository.save(project);

		List<UserId> allMemberIds = memberIds != null ? memberIds : List.of();
		allMemberIds = allMemberIds.stream()
			.filter(id -> !id.id().equals(ownerUserId.id()))
			.collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));

		List<User> userRefs = userRepository.getReferenceByIdIsIn(allMemberIds);
		List<ProjectMember> members = userRefs.stream().map(user -> ProjectMember.builder()
			.id(ProjectMemberId.generateId())
			.project(savedProject)
			.user(user)
			.joinedAt(LocalDateTime.now())
			.build()).collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));

		savedProject.setMembers(members);
		return projectRepository.save(savedProject);
	}
}
