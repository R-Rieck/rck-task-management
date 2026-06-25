package com.rrieck.taskmanagementbackend.auth.exception.refreshToken;

public class RefreshTokenInvalid extends RefreshTokenException {
    public RefreshTokenInvalid() {
        super("REFRESH_TOKEN_INVALID", "Refresh token is invalid");
    }
}
