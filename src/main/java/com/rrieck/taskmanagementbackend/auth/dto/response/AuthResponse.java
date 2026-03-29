package com.rrieck.taskmanagementbackend.auth.dto.response;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import lombok.Builder;

@Builder
public record AuthResponse(
	String accessToken,
	String refreshToken,
	UserId userId,
	AccountId accountId
) {
}
