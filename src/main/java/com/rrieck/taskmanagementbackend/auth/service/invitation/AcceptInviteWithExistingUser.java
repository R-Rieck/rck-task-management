package com.rrieck.taskmanagementbackend.auth.service.invitation;

import com.rrieck.taskmanagementbackend.auth.exception.invitation.InvitationExpired;
import com.rrieck.taskmanagementbackend.auth.exception.invitation.InvitationTokenNotFound;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.invitation.Invitation;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.InvitationRepository;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.accountMember.CreateAccountMember;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.IssueNewTokenPairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcceptInviteWithExistingUser {
	private final InvitationRepository invitationRepository;
	private final CreateAccountMember createAccountMember;
	private final IssueNewTokenPairService issueNewTokenPairService;

	public AuthTypes.AuthType accept(UUID invitationToken, UserId userId) {
		Invitation invitation = invitationRepository
			.getByInvitationCode(invitationToken)
			.orElseThrow(() -> new InvitationTokenNotFound(invitationToken));

		if (invitation.getExpirationDate().isBefore(java.time.LocalDateTime.now())) {
			throw new InvitationExpired();
		}

		createAccountMember.create(
			invitation.getInvitedByAccount(),
			userId,
			Role.User
		);

		invitationRepository.delete(invitation);

		TokenPair tokens = issueNewTokenPairService.issue(userId, invitation.getInvitedByAccount());

		return AuthTypes.AuthType
			.builder()
			.accessToken(tokens.getAccessToken())
			.refreshToken(tokens.getRefreshToken())
			.userId(userId)
			.accountId(invitation.getInvitedByAccount())
			.build();
	}
}
