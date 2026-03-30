package com.rrieck.taskmanagementbackend.account.exception;

import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import org.springframework.http.HttpStatus;

public class AccountNotFound extends OutgoingException {
	public AccountNotFound() {
		super("ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND, "Account not found");
	}
}
