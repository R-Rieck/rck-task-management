package com.rrieck.taskmanagementbackend.auth.exception.refreshToken;

import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import org.springframework.http.HttpStatus;

public abstract class RefreshTokenException extends OutgoingException {
    protected RefreshTokenException(String errorCode, String message) {
        super(errorCode, HttpStatus.UNAUTHORIZED, message);
    }
}
