package com.rrieck.taskmanagementbackend.auth.exception.user;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.common.error.InternalException;

public class UserNotFound extends InternalException {
	public UserNotFound(UserId id) {
		super("user with ID: [" + id + "] not found");
	}
}
