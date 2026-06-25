package com.rrieck.taskmanagementbackend.auth.exception.refreshToken;

public class RefreshTokenExpired extends RefreshTokenException {
    public RefreshTokenExpired() {
        super("REFRESH_TOKEN_EXPIRED", "Refresh token has expired");
    }
}
