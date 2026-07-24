package com.rrieck.taskmanagementbackend.project.repository;

import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, ProjectId> {
	@Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.members m WHERE p.account.id = :accountId AND (p.owner.id.id = :userId OR m.user.id.id = :userId)")
	List<Project> findAccessibleByAccount(@Param("accountId") UUID accountId, @Param("userId") UUID userId);
}
