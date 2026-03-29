package com.rrieck.taskmanagementbackend.account.dto.reponse;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.model.AccountId;
import lombok.Builder;

@Builder
public record AccountResponse(
	AccountId id,
	String name
) {
	public static AccountResponse from(Account account) {
		return AccountResponse
			.builder()
			.id(account.getId())
			.name(account.getName())
			.build();
	}
}
