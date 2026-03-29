package com.rrieck.taskmanagementbackend.accountMemeber.repository;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMember;
import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMemberId;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountMemberRepository extends JpaRepository<AccountMember, AccountMemberId> {
	public List<AccountMember> getAllByAccountId(AccountId accountId);

	public AccountMember getByAccountIdAndUserId(AccountId accountId, UserId userId);
}
