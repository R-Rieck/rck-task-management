package com.rrieck.taskmanagementbackend.common.identifier;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class Identifier<T> {
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	public Identifier() {
	}

	protected Identifier(UUID id) {
		this.id = id;
	}

	public UUID id() {
		return id;
	}
}
