package com.rrieck.taskmanagementbackend.auth.service.account;

import com.rrieck.taskmanagementbackend.auth.model.account.Account;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountRepository;
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
