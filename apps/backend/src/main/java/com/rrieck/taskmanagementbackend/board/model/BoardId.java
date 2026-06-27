package com.rrieck.taskmanagementbackend.board.model;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public final class BoardId extends Identifier {
	public BoardId(UUID id) { super(id); }
	public BoardId() {}
	public static BoardId fromString(String value) { return new BoardId(UUID.fromString(value)); }
	public static BoardId generateId() { return new BoardId(UUID.randomUUID()); }
}
