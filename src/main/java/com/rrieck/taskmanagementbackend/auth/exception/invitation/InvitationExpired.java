package com.rrieck.taskmanagementbackend.auth.exception.invitation;

import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import org.springframework.http.HttpStatus;

public class InvitationExpired extends OutgoingException {
	public InvitationExpired() {
		super(
			"TOKEN_EXPIRED",
			HttpStatus.CONFLICT,
			"token is expired"
		);
	}
}
