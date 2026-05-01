package com.rrieck.taskmanagementbackend.auth.service.accountMember;

import com.rrieck.taskmanagementbackend.auth.exception.accountMember.AccountMemberNotFound;
import com.rrieck.taskmanagementbackend.auth.exception.accountMember.AccountNeedsAtLeastOneAdmin;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.AccountRepository;
import com.rrieck.taskmanagementbackend.auth.schema.accountMembers.AccountMemberTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditMemberRoleService {
	private final AccountMemberRepository accountMemberRepository;
	private final AccountRepository accountRepository;

	public AccountMemberTypes.AccountMemberType EditMemberRoleService(AccountId accountId, UserId userId, Role newRole) {
		AccountMember memberToChange = accountMemberRepository
			.getOptByAccountIdAndUserId(accountId, userId)
			.orElseThrow(() -> new AccountMemberNotFound(accountId, userId));

		List<AccountMember> members = accountMemberRepository.getByAccountIdAndRole(accountId, Role.Admin);

		boolean isRemovingOnlyAdmin =
			memberToChange.getRole() == Role.Admin &&
				newRole != Role.Admin &&
				members.size() == 1;

		if (isRemovingOnlyAdmin) {
			throw new AccountNeedsAtLeastOneAdmin();
		}

		memberToChange.setRole(newRole);

		accountMemberRepository.save(memberToChange);

		return AccountMemberTypes.AccountMemberType.from(memberToChange);
	}
}
