package com.rrieck.taskmanagementbackend.auth.exception.invitation;

import com.rrieck.taskmanagementbackend.common.error.InternalException;

import java.util.UUID;

public class InvitationTokenNotFound extends InternalException {
	public InvitationTokenNotFound(UUID invitationToken) {
		super("InvitationToken: [" + invitationToken + "] not found");
	}
}
