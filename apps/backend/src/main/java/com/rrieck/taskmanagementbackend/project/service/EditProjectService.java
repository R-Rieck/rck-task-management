package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EditProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	public void edit(ProjectId projectId, String newName, Optional<String> newDescription, UserId ownerId) {
		User ownerRef = userRepository.getReferenceById(ownerId);
		Project existingProject = projectRepository.findById(projectId).orElseThrow();

		existingProject.setName(newName);
		existingProject.setOwner(ownerRef);
		existingProject.setUpdatedAt(LocalDateTime.now());

		newDescription.ifPresent(existingProject::setDescription);

		projectRepository.save(existingProject);
	}
}
