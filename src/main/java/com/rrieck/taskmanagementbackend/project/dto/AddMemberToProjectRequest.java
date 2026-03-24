package com.rrieck.taskmanagementbackend.project.dto;

import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.user.model.UserId;

import java.util.List;


public record AddMemberToProjectRequest(
	ProjectId projectId,
	List<UserId> members
) {
}
