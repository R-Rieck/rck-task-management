package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EditProjectService {
	private final ProjectRepository projectRepository;

	public Project edit(ProjectId projectId, String newName, String description, boolean isPrivate, String icon) {
		Project existingProject = projectRepository.findById(projectId).orElseThrow();

		existingProject.setName(newName);
		existingProject.setDescription(description);
		existingProject.setPrivate(isPrivate);
		existingProject.setIcon(icon);
		existingProject.setUpdatedAt(LocalDateTime.now());

		return projectRepository.save(existingProject);
	}
}
