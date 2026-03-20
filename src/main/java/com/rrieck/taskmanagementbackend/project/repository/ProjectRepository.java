package com.rrieck.taskmanagementbackend.project.repository;

import com.rrieck.taskmanagementbackend.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {}
