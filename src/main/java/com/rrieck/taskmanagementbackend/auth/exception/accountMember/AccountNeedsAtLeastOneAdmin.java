package com.rrieck.taskmanagementbackend.auth.exception.accountMember;

import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import org.springframework.http.HttpStatus;

public class AccountNeedsAtLeastOneAdmin extends OutgoingException {
	public AccountNeedsAtLeastOneAdmin() {
		super("ACCOUNT_NEEDS_ONE_ADMIN", HttpStatus.CONFLICT, "account needs at least one admin");
	}
}
