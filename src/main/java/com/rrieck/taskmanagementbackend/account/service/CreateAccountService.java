package com.rrieck.taskmanagementbackend.account.service;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAccountService {
	private final AccountRepository accountRepository;

	public AccountId create(String name) {
		AccountId accountId = AccountId.generateId();
		Account account = Account.builder().id(accountId).name(name).build();

		accountRepository.save(account);

		return accountId;
	}
}
