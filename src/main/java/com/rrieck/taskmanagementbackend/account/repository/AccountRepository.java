package com.rrieck.taskmanagementbackend.account.repository;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.model.AccountId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, AccountId> {
}
