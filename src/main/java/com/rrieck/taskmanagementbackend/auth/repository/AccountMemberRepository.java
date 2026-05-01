package com.rrieck.taskmanagementbackend.auth.repository;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMemberId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountMemberRepository extends JpaRepository<AccountMember, AccountMemberId> {
	List<AccountMember> getAllByAccountId(AccountId accountId);

	List<AccountMember> getAllByUserId(UserId userId);

	AccountMember getByAccountIdAndUserId(AccountId accountId, UserId userId);

	Optional<AccountMember> getOptByAccountIdAndUserId(AccountId accountId, UserId userId);

	List<AccountMember> getByAccountIdAndRole(AccountId accountId, Role role);


}
