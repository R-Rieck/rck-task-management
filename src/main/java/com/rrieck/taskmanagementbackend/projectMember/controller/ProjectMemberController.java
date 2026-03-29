package com.rrieck.taskmanagementbackend.projectMember.controller;

import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.projectMember.dto.AddMemberToProjectRequest;
import com.rrieck.taskmanagementbackend.projectMember.dto.ProjectWithMemberResponse;
import com.rrieck.taskmanagementbackend.projectMember.service.AddMemberToProjectService;
import com.rrieck.taskmanagementbackend.projectMember.service.GetProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project-member")
@RequiredArgsConstructor
public class ProjectMemberController {
	private final GetProjectMemberService getProjectMemberService;
	private final AddMemberToProjectService addMemberToProjectService;

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
}
