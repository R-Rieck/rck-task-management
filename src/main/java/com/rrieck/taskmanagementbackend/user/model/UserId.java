package com.rrieck.taskmanagementbackend.user.model;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public final class UserId extends Identifier<UserId> {
	public UserId(UUID id) {
		super(id);
	}

	public UserId() {
	}

	public static UserId fromString(String value) {
		return new UserId(UUID.fromString(value));
	}

	public static UserId generateId() {
		return new UserId(UUID.randomUUID());
	}
}

