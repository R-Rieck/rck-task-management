package com.rrieck.taskmanagementbackend.board.model;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public final class BoardSectionId extends Identifier {
	public BoardSectionId(UUID id) { super(id); }
	public BoardSectionId() {}
	public static BoardSectionId fromString(String value) { return new BoardSectionId(UUID.fromString(value)); }
	public static BoardSectionId generateId() { return new BoardSectionId(UUID.randomUUID()); }
}
