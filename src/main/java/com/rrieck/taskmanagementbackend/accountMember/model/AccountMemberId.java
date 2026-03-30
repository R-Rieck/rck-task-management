package com.rrieck.taskmanagementbackend.accountMember.model;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class AccountMemberId extends Identifier {
	public AccountMemberId(UUID id) {
		super(id);
	}

	public AccountMemberId() {
	}

	public static AccountMemberId fromString(String value) {
		return new AccountMemberId(UUID.fromString(value));
	}

	public static AccountMemberId generateId() {
		return new AccountMemberId(UUID.randomUUID());
	}
}
