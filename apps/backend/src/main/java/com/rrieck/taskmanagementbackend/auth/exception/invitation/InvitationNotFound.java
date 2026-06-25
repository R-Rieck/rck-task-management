package com.rrieck.taskmanagementbackend.auth.exception.invitation;

import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import org.springframework.http.HttpStatus;

public class InvitationNotFound extends OutgoingException {
	public InvitationNotFound() {
		super("INVITATION_NOT_FOUND", HttpStatus.NOT_FOUND, "Invitation not found");
	}
}
