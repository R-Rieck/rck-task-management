package com.rrieck.taskmanagementbackend.project.controller;

import com.rrieck.taskmanagementbackend.project.dto.*;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.service.*;
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
	private final GetProjectMemberService getProjectMemberService;
	private final AddMemberToProjectService addMemberToProjectService;

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

	@PostMapping("/memebers/{projectId}")
	public ProjectWithMemberResponse getMembers(
		@PathVariable String projectId,
		Authentication authentication
	) {
		return getProjectMemberService.get(
			ProjectId.fromString(projectId)
		);
	}

	@PostMapping("/memebers/add")
	public ProjectWithMemberResponse getMembers(
		@RequestBody AddMemberToProjectRequest request,
		Authentication authentication
	) {
		return addMemberToProjectService.add(
			request.members(),
			request.projectId()
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
