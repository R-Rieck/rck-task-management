package com.rrieck.taskmanagementbackend.accountMemeber.service;

import com.rrieck.taskmanagementbackend.account.exception.AccountNotFound;
import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMember;
import com.rrieck.taskmanagementbackend.accountMemeber.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.accountMemeber.schema.AccountMemberTypes;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAccountMember {
	private final AccountMemberRepository accountMemberRepository;

	public AccountMemberTypes.AccountMemberResponse get(AccountId accountId) {
		List<AccountMember> accountMembers = accountMemberRepository.getAllByAccountId(accountId);
		Account account = accountMembers
			.stream()
			.findFirst()
			.map(AccountMember::getAccount)
			.orElseThrow(() -> new AccountNotFound());

		List<AccountMember> accountMember = accountMembers
			.stream()
			.toList();

		Map<User, Role> member = accountMember
			.stream()
			.sorted(Comparator.comparing(AccountMember::getRole, Comparator.reverseOrder()))
			.collect(Collectors.toMap(
				AccountMember::getUser,
				AccountMember::getRole,
				(a, b) -> a,
				LinkedHashMap::new
			));


		return AccountMemberTypes.AccountMemberResponse.from(
			account,
			member
		);
	}
}
