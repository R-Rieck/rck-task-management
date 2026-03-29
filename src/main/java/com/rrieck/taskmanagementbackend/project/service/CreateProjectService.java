package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.project.dto.ProjectResponse;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	public ProjectResponse create(String name, Optional<String> description, UserId ownerUserId) {
		User ownerRef = userRepository.getReferenceById(ownerUserId);
		Project project = Project
			.builder()
			.id(ProjectId.generateId())
			.name(name)
			.owner(ownerRef)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		description.ifPresent(project::setDescription);

		projectRepository.save(project);

		return ProjectResponse.from(project);
	}
}
