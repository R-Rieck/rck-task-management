package com.rrieck.taskmanagementbackend.auth.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthResponse(
        String accessToken,
        String refreshToken,
        UUID userId
) {
}
