package com.rrieck.taskmanagementbackend.auth.schema.account;

import com.rrieck.taskmanagementbackend.auth.model.account.Account;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

public class AccountTypes {
	@Builder
	public record AccountType(
		AccountId id,
		String name
	) {
		public static AccountType from(
			@NonNull Account account
		) {
			return AccountType
				.builder()
				.id(account.getId())
				.name(account.getName())
				.build();
		}
	}

	public record SwitchAccountInput(AccountId toAccountId) {
	}

}
