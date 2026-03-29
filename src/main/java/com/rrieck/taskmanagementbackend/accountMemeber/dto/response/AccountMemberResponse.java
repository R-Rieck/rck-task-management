package com.rrieck.taskmanagementbackend.accountMemeber.dto.response;

import com.rrieck.taskmanagementbackend.account.dto.reponse.AccountResponse;
import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import com.rrieck.taskmanagementbackend.user.model.User;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record AccountMemberResponse(
	AccountResponse account,
	List<MemberWithRole> member
) {
	public static AccountMemberResponse from(Account account, Map<User, Role> members) {
		return AccountMemberResponse
			.builder()
			.account(AccountResponse.from(account))
			.member(
				members
					.entrySet()
					.stream()
					.map(entry ->
						MemberWithRole.from(
							UserResponse.from(entry.getKey()),
							entry.getValue()
						)
					)
					.toList()
			)
			.build();
	}
}
