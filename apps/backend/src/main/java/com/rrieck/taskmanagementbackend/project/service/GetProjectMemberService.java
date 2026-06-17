package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMember;
import com.rrieck.taskmanagementbackend.project.repository.ProjectMemberRepository;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProjectMemberService {
	private final ProjectMemberRepository projectMemberRepository;
	private final ProjectRepository projectRepository;

	public void get(ProjectId projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow();
		List<ProjectMember> projectMembers = projectMemberRepository.findAllByProjectId(projectId);
	}
}
