package com.rrieck.taskmanagementbackend.user.exception;

import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyRegistered extends OutgoingException {
	public EmailAlreadyRegistered() {
		super(
			"EMAIL_ALREADY_REGISTERED",
			HttpStatus.CONFLICT,
			"Email already registered"
		);
	}
}
