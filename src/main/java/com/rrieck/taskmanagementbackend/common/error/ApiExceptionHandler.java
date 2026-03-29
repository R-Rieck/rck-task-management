package com.rrieck.taskmanagementbackend.common.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(OutgoingException.class)
	public ResponseEntity<OutgoingExceptionResponse> handleOutgoingException(OutgoingException exception, HttpServletRequest request) {
		return buildErrorResponse(
			exception.getStatus(),
			exception.getErrorCode(),
			exception.getMessage(),
			request
		);
	}

	@ExceptionHandler(InternalException.class)
	public ResponseEntity<OutgoingExceptionResponse> handleInternalException(Exception exception, HttpServletRequest request) {
		return buildErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR,
			"INTERNAL_SERVER_ERROR",
			"An unexpected error occurred",
			request
		);
	}

	private ResponseEntity<OutgoingExceptionResponse> buildErrorResponse(
		HttpStatus status,
		String errorCode,
		String message,
		HttpServletRequest request
	) {
		OutgoingExceptionResponse response = new OutgoingExceptionResponse(
			errorCode,
			message,
			status.value(),
			OffsetDateTime.now(),
			request.getRequestURI()
		);

		return ResponseEntity.status(status).body(response);
	}
}
