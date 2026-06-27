package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	public Project create(String name, String description, boolean isPrivate, AccountId accountId, UserId ownerUserId) {
		User ownerRef = userRepository.getReferenceById(ownerUserId);
		Project project = Project
			.builder()
			.id(ProjectId.generateId())
			.name(name)
			.description(description)
			.isPrivate(isPrivate)
			.account(accountId)
			.owner(ownerRef)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		return projectRepository.save(project);
	}
}
