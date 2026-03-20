package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.project.dto.ProjectResponse;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	public ProjectResponse create(String name, Optional<String> description, UUID ownerUserId) {
		User ownerRef = userRepository.getReferenceById(ownerUserId);
		Project project = Project
			.builder()
			.name(name)
			.owner(ownerRef)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		if (description.isPresent()) {
			project.setDescription(description.get());
		}

		projectRepository.save(project);

		return ProjectResponse
			.builder()
			.id(project.getId())
			.name(project.getName())
			.description(description)
			.owner(project.getOwner())
			.build();
	}
}
