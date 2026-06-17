package com.rrieck.taskmanagementbackend.auth.service.accountMember;

import com.rrieck.taskmanagementbackend.auth.exception.account.AccountNotFound;
import com.rrieck.taskmanagementbackend.auth.exception.accountMember.UserIsAlreadyMember;
import com.rrieck.taskmanagementbackend.auth.exception.user.UserNotFound;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.account.Account;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMemberId;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.AccountRepository;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateAccountMember {
	private final AccountMemberRepository accountMemberRepository;
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;

	public void create(AccountId accountId, UserId userId, Role role) {
		Optional<User> userRef = userRepository.getOptById(userId);
		Optional<Account> accountRef = accountRepository.getOptById(accountId);
		Optional<AccountMember> existingAccountMember = accountMemberRepository.getOptByAccountIdAndUserId(accountId, userId);

		if (userRef.isEmpty()) {
			throw new UserNotFound(userId);
		}

		if (accountRef.isEmpty()) {
			throw new AccountNotFound(accountId);
		}

		if (existingAccountMember.isPresent()) {
			throw new UserIsAlreadyMember();
		}

		AccountMember accountMember = AccountMember
			.builder()
			.id(AccountMemberId.generateId())
			.account(accountRef.get())
			.user(userRef.get())
			.role(role)
			.build();

		accountMemberRepository.save(accountMember);
	}
}
