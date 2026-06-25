package com.rrieck.taskmanagementbackend.auth.exception.refreshToken;

public class RefreshTokenNotFound extends RefreshTokenException {
    public RefreshTokenNotFound(String token) {
        super("REFRESH_TOKEN_NOT_FOUND", "Refresh token not found");
    }
}
