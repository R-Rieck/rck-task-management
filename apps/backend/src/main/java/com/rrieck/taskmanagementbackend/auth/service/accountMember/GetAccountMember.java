package com.rrieck.taskmanagementbackend.auth.service.accountMember;

import com.rrieck.taskmanagementbackend.auth.exception.account.AccountNotFound;
import com.rrieck.taskmanagementbackend.auth.model.account.Account;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.invitation.Invitation;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.AccountRepository;
import com.rrieck.taskmanagementbackend.auth.repository.InvitationRepository;
import com.rrieck.taskmanagementbackend.auth.schema.accountMembers.AccountMemberTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAccountMember {
	private final AccountMemberRepository accountMemberRepository;
	private final InvitationRepository invitationRepository;
	private final AccountRepository accountRepository;

	public AccountMemberTypes.AccountMemberWithInvitationType get(
		AccountId accountId
	) {
		Account account = accountRepository.getOptById(accountId).orElseThrow(() -> new AccountNotFound(accountId));
		List<AccountMember> accountMembers = accountMemberRepository.getAllByAccountId(accountId);
		List<Invitation> openInvitations = invitationRepository.getByInvitedByAccount(accountId);

		List<AccountMember> sortedMembers = accountMembers
			.stream()
			.sorted(Comparator.comparing(AccountMember::getRole).reversed())
			.toList();

		return AccountMemberTypes.AccountMemberWithInvitationType.from(account.getName(), sortedMembers, openInvitations);
	}
}
