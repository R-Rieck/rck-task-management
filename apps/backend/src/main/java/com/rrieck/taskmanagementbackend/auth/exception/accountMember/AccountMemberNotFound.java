package com.rrieck.taskmanagementbackend.auth.exception.accountMember;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.common.error.InternalException;

public class AccountMemberNotFound extends InternalException {
	public AccountMemberNotFound(AccountId accountId, UserId userId) {
		super("user with ID: [" + userId + "] in account with ID: [" + accountId + "] not found");
	}
}
