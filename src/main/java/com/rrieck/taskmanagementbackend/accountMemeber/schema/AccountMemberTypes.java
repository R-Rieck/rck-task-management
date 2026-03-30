package com.rrieck.taskmanagementbackend.accountMemeber.schema;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.schema.AccountTypes;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.schema.UserTypes;
import lombok.Builder;

import java.util.List;
import java.util.Map;


public class AccountMemberTypes {
	@Builder
	public record AccountMemberResponse(
		AccountTypes.AccountType account,
		List<MemberWithRole> member
	) {
		public static AccountMemberResponse from(Account account, Map<User, Role> members) {
			return AccountMemberResponse
				.builder()
				.account(AccountTypes.AccountType.from(account))
				.member(
					members
						.entrySet()
						.stream()
						.map(entry ->
							MemberWithRole.from(
								UserTypes.UserResponse.from(entry.getKey()),
								entry.getValue()
							)
						)
						.toList()
				)
				.build();
		}


	}

	@Builder
	public record MemberWithRole(
		UserTypes.UserResponse user,
		Role role
	) {
		public static MemberWithRole from(UserTypes.UserResponse user, Role role) {
			return MemberWithRole
				.builder()
				.user(user)
				.role(role)
				.build();
		}
	}
}