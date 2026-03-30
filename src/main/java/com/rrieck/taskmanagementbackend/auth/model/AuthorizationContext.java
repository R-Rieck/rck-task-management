package com.rrieck.taskmanagementbackend.auth.model;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import lombok.Builder;

@Builder
public record AuthorizationContext(
	UserId userId,
	AccountId accountId,
	String email
) {
}
