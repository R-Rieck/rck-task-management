package com.rrieck.taskmanagementbackend.common.error;

public abstract class InternalException extends RuntimeException {
	protected InternalException(String message) {
		super(message);
	}
}
