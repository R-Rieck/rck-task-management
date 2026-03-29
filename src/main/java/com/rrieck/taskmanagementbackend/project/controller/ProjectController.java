package com.rrieck.taskmanagementbackend.project.controller;

import com.rrieck.taskmanagementbackend.project.dto.CreateProjectRequest;
import com.rrieck.taskmanagementbackend.project.dto.EditProjectRequest;
import com.rrieck.taskmanagementbackend.project.dto.ProjectResponse;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.service.CreateProjectService;
import com.rrieck.taskmanagementbackend.project.service.DeleteProjectService;
import com.rrieck.taskmanagementbackend.project.service.EditProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
	private final CreateProjectService createProjectService;
	private final EditProjectService editProjectService;
	private final DeleteProjectService deleteProjectService;

	@PostMapping
	public ProjectResponse create(
		@RequestBody CreateProjectRequest request,
		Authentication authentication
	) {
		return createProjectService.create(
			request.name(),
			request.description(),
			request.owner()
		);
	}

	@PatchMapping("/{projectId}")
	public ProjectResponse edit(
		@PathVariable ProjectId projectId,
		@RequestBody EditProjectRequest request,
		Authentication authentication
	) {
		return editProjectService.edit(
			projectId,
			request.newName(),
			request.newDescription(),
			request.newOwner()
		);

	}

	@DeleteMapping("/{projectId}")
	public void delete(
		@PathVariable ProjectId projectId,
		Authentication authentication
	) {
		deleteProjectService.delete(projectId);
	}
}
