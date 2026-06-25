package com.rrieck.taskmanagementbackend.common.config;

import com.rrieck.taskmanagementbackend.auth.exception.refreshToken.RefreshTokenException;
import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Map;

@ControllerAdvice
public class GraphQlExceptionHandlerAdvice {

	@GraphQlExceptionHandler(RefreshTokenException.class)
	public GraphQLError handleRefreshTokenException(
		RefreshTokenException exception,
		DataFetchingEnvironment environment
	) {
		return GraphqlErrorBuilder.newError(environment)
		                          .errorType(ErrorType.UNAUTHORIZED)
		                          .message(exception.getMessage())
		                          .extensions(Map.of(
			                          "code", exception.getErrorCode(),
			                          "httpStatus", 401
		                          ))
		                          .build();
	}

	@GraphQlExceptionHandler(OutgoingException.class)
	public GraphQLError handleOutgoingException(
		OutgoingException exception,
		DataFetchingEnvironment environment
	) {
		return GraphqlErrorBuilder.newError(environment)
		                          .errorType(ErrorType.BAD_REQUEST)
		                          .message(exception.getMessage())
		                          .extensions(Map.of(
			                          "code", exception.getErrorCode(),
			                          "httpStatus", exception.getStatus().value()
		                          ))
		                          .build();
	}

	@GraphQlExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public GraphQLError handleMissingAuthentication(
		AuthenticationCredentialsNotFoundException exception,
		DataFetchingEnvironment environment
	) {
		return GraphqlErrorBuilder.newError(environment)
		                          .errorType(ErrorType.UNAUTHORIZED)
		                          .message("Authentication is required")
		                          .extensions(Map.of(
			                          "code", "AUTHENTICATION_REQUIRED",
			                          "httpStatus", 401
		                          ))
		                          .build();
	}

	@GraphQlExceptionHandler(AuthenticationException.class)
	public GraphQLError handleAuthenticationException(
		AuthenticationException exception,
		DataFetchingEnvironment environment
	) {
		return GraphqlErrorBuilder.newError(environment)
		                          .errorType(ErrorType.UNAUTHORIZED)
		                          .message("Invalid email or password")
		                          .extensions(Map.of(
			                          "code", "INVALID_CREDENTIALS",
			                          "httpStatus", 401
		                          ))
		                          .build();
	}

	@GraphQlExceptionHandler(Exception.class)
	public GraphQLError handleException(
		Exception exception,
		DataFetchingEnvironment environment
	) {
		return GraphqlErrorBuilder.newError(environment)
		                          .errorType(ErrorType.INTERNAL_ERROR)
		                          .message("An unexpected error occurred")
		                          .build();
	}
}
