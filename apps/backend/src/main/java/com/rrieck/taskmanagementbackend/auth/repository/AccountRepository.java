package com.rrieck.taskmanagementbackend.auth.repository;

import com.rrieck.taskmanagementbackend.auth.model.account.Account;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, AccountId> {
	public Optional<Account> getOptById(AccountId id);
}
