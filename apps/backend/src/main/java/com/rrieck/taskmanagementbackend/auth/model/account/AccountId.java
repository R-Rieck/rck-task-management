package com.rrieck.taskmanagementbackend.auth.model.account;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class AccountId extends Identifier {
	public AccountId(UUID id) {
		super(id);
	}

	public AccountId() {
	}

	public static AccountId fromString(String value) {
		return new AccountId(UUID.fromString(value));
	}

	public static AccountId generateId() {
		return new AccountId(UUID.randomUUID());
	}
}
