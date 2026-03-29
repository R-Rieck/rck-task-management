package com.rrieck.taskmanagementbackend.common.error;

import java.time.OffsetDateTime;

public record OutgoingExceptionResponse(
	String errorCode,
	String message,
	int status,
	OffsetDateTime timestamp,
	String path
) {
}
