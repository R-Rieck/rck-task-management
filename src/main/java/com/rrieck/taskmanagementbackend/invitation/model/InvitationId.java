package com.rrieck.taskmanagementbackend.invitation.model;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class InvitationId extends Identifier {
	public InvitationId(UUID id) {
		super(id);
	}

	public InvitationId() {
	}

	public static InvitationId fromString(String value) {
		return new InvitationId(UUID.fromString(value));
	}

	public static InvitationId generateId() {
		return new InvitationId(UUID.randomUUID());
	}
}
