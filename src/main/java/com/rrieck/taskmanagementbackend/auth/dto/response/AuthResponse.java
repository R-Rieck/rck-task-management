package com.rrieck.taskmanagementbackend.auth.dto.response;

import com.rrieck.taskmanagementbackend.user.model.UserId;
import lombok.Builder;

@Builder
public record AuthResponse(
	String accessToken,
	String refreshToken,
	UserId userId
) {
}
