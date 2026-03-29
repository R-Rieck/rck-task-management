package com.rrieck.taskmanagementbackend.accountMemeber.service;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.account.repository.AccountRepository;
import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMember;
import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMemberId;
import com.rrieck.taskmanagementbackend.accountMemeber.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAccountMember {
	private final AccountMemberRepository accountMemberRepository;
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;

	public void create(AccountId accountId, UserId userId, Role role) {
		User userRef = userRepository.getReferenceById(userId);
		Account accountRef = accountRepository.getReferenceById(accountId);

		AccountMember accountMember = AccountMember
			.builder()
			.id(AccountMemberId.generateId())
			.account(accountRef)
			.user(userRef)
			.role(role)
			.build();

		accountMemberRepository.save(accountMember);
	}
}
