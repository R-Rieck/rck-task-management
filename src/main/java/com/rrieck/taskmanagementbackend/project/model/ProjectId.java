package com.rrieck.taskmanagementbackend.project.model;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public final class ProjectId extends Identifier {
	public ProjectId(UUID id) {
		super(id);
	}

	public ProjectId() {
	}

	public static ProjectId fromString(String value) {
		return new ProjectId(UUID.fromString(value));
	}

	public static ProjectId generateId() {
		return new ProjectId(UUID.randomUUID());
	}
}
