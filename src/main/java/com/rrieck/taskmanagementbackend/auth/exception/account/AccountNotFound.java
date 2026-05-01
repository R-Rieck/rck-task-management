package com.rrieck.taskmanagementbackend.auth.exception.account;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.common.error.InternalException;

public class AccountNotFound extends InternalException {
	public AccountNotFound(AccountId id) {
		super("account with ID: [" + id + "] not found");
	}
}
