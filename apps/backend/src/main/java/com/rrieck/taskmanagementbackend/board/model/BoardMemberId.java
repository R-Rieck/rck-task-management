package com.rrieck.taskmanagementbackend.board.model;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public final class BoardMemberId extends Identifier {
	public BoardMemberId(UUID id) { super(id); }
	public BoardMemberId() {}
	public static BoardMemberId fromString(String value) { return new BoardMemberId(UUID.fromString(value)); }
	public static BoardMemberId generateId() { return new BoardMemberId(UUID.randomUUID()); }
}
