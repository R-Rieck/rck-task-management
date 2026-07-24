package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserProjectsService {
	private final ProjectRepository projectRepository;

	@Transactional(readOnly = true)
	public List<Project> getProjects(AccountId accountId, UserId userId) {
		return projectRepository.findAccessibleByAccount(accountId.id(), userId.id());
	}
}
