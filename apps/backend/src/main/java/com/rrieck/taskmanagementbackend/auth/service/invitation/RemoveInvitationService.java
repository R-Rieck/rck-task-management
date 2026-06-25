package com.rrieck.taskmanagementbackend.auth.service.invitation;

import com.rrieck.taskmanagementbackend.auth.exception.accountMember.AccountMemberNotAdmin;
import com.rrieck.taskmanagementbackend.auth.exception.accountMember.AccountMemberNotFound;
import com.rrieck.taskmanagementbackend.auth.exception.invitation.InvitationNotFound;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.invitation.Invitation;
import com.rrieck.taskmanagementbackend.auth.model.invitation.InvitationId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemoveInvitationService {
	private final InvitationRepository invitationRepository;
	private final AccountMemberRepository accountMemberRepository;

	@Transactional
	public boolean remove(AccountId accountId, UserId userId, InvitationId invitationId) {
		Invitation invitation = invitationRepository
			.findById(invitationId)
			.orElseThrow(InvitationNotFound::new);

		if (!invitation.getInvitedByAccount().id().equals(accountId.id())) {
			throw new InvitationNotFound();
		}

		AccountMember requester = accountMemberRepository
			.getOptByAccountIdAndUserId(accountId, userId)
			.orElseThrow(() -> new AccountMemberNotFound(accountId, userId));

		if (requester.getRole() != Role.Admin) {
			throw new AccountMemberNotAdmin();
		}

		invitationRepository.delete(invitation);

		return true;
	}
}
