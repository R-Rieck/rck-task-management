package com.rrieck.taskmanagementbackend.projectMember.repository;

import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.projectMember.model.ProjectMember;
import com.rrieck.taskmanagementbackend.projectMember.model.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
	public List<ProjectMember> findAllByProjectId(ProjectId projectId);
}
