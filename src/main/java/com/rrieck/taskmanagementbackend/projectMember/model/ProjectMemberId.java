package com.rrieck.taskmanagementbackend.projectMember.model;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public final class ProjectMemberId extends Identifier {
	public ProjectMemberId(UUID id) {
		super(id);
	}

	public ProjectMemberId() {
	}

	public static ProjectMemberId fromString(String value) {
		return new ProjectMemberId(UUID.fromString(value));
	}

	public static ProjectMemberId generateId() {
		return new ProjectMemberId(UUID.randomUUID());
	}
}
