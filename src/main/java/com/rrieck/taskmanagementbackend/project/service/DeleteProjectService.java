package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	public void delete(UUID projectId) {
		projectRepository.deleteById(projectId);
	}
}
