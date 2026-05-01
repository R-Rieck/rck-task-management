package com.rrieck.taskmanagementbackend.auth.model;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import lombok.Builder;

@Builder
public record AuthorizationContext(
	UserId userId,
	AccountId accountId,
	String email
) {
}
