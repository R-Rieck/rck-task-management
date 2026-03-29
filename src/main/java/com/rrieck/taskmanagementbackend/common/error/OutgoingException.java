package com.rrieck.taskmanagementbackend.common.error;

import org.springframework.http.HttpStatus;

public abstract class OutgoingException extends RuntimeException {
	private final String errorCode;
	private final HttpStatus status;

	protected OutgoingException(String errorCode, HttpStatus status, String message) {
		super(message);
		this.errorCode = errorCode;
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
