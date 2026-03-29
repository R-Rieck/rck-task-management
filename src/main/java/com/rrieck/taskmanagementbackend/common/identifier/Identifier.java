package com.rrieck.taskmanagementbackend.common.identifier;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class Identifier {
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	public Identifier() {
	}

	protected Identifier(UUID id) {
		this.id = id;
	}

	@JsonValue
	public UUID value() {
		return id;
	}

	public UUID id() {
		return id;
	}
}
