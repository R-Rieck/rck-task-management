package com.rrieck.taskmanagementbackend.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshAuthenticationRequest(
	@NotBlank String refreshToken
) {
}
