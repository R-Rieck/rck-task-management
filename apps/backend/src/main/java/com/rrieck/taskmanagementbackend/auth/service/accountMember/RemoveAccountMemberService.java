package com.rrieck.taskmanagementbackend.auth.service.accountMember;

import com.rrieck.taskmanagementbackend.auth.exception.accountMember.AccountMemberNotAdmin;
import com.rrieck.taskmanagementbackend.auth.exception.accountMember.AccountMemberNotFound;
import com.rrieck.taskmanagementbackend.auth.exception.accountMember.AccountNeedsAtLeastOneAdmin;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RemoveAccountMemberService {
	private final AccountMemberRepository accountMemberRepository;
	private final UserRepository userRepository;

	@Transactional
	public boolean remove(AccountId accountId, UserId targetUserId, UserId requestingUserId) {
		boolean isRemovingSelf = targetUserId.equals(requestingUserId);

		if (!isRemovingSelf) {
			AccountMember requester = accountMemberRepository
				.getOptByAccountIdAndUserId(accountId, requestingUserId)
				.orElseThrow(() -> new AccountMemberNotFound(accountId, requestingUserId));

			if (requester.getRole() != Role.Admin) {
				throw new AccountMemberNotAdmin();
			}
		}

		AccountMember member = accountMemberRepository
			.getOptByAccountIdAndUserId(accountId, targetUserId)
			.orElseThrow(() -> new AccountMemberNotFound(accountId, targetUserId));

		if (member.getRole() == Role.Admin) {
			List<AccountMember> admins = accountMemberRepository
				.getByAccountIdAndRole(accountId, Role.Admin);

			if (admins.size() <= 1) {
				throw new AccountNeedsAtLeastOneAdmin();
			}
		}

		accountMemberRepository.delete(member);

		List<AccountMember> remainingMemberships = accountMemberRepository.getAllByUserId(targetUserId);

		if (!remainingMemberships.isEmpty()) {
			AccountId newAccountId = remainingMemberships.get(0).getAccount().getId();
			userRepository.getOptById(targetUserId).ifPresent(user -> {
				user.setLastUsedAccountId(newAccountId);
				userRepository.save(user);
			});
		}

		return true;
	}
}
