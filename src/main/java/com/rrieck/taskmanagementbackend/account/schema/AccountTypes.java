package com.rrieck.taskmanagementbackend.account.schema;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.model.AccountId;
import lombok.Builder;

public class AccountTypes {
	@Builder
	public record AccountType(
		AccountId id,
		String name
	) {
		public static AccountType from(Account account) {
			return AccountType
				.builder()
				.id(account.getId())
				.name(account.getName())
				.build();
		}
	}

}
