package com.rrieck.taskmanagementbackend.auth.exception.refreshToken;

public class RefreshTokenRevoked extends RefreshTokenException {
    public RefreshTokenRevoked() {
        super("REFRESH_TOKEN_REVOKED", "Refresh token has been revoked");
    }
}
