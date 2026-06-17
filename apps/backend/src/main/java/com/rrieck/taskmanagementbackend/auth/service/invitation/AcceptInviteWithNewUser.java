package com.rrieck.taskmanagementbackend.auth.service.invitation;

import com.rrieck.taskmanagementbackend.auth.exception.invitation.InvitationExpired;
import com.rrieck.taskmanagementbackend.auth.exception.invitation.InvitationTokenNotFound;
import com.rrieck.taskmanagementbackend.auth.model.invitation.Invitation;
import com.rrieck.taskmanagementbackend.auth.repository.InvitationRepository;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.authentication.RegisterUserInAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcceptInviteWithNewUser {
	private final RegisterUserInAccountService registerUserInAccountService;
	private final InvitationRepository invitationRepository;

	public AuthTypes.AuthType accept(
		UUID invitationToken,
		String name,
		String email,
		String password
	) {
		Invitation invitation = invitationRepository
			.getByInvitationCode(invitationToken)
			.orElseThrow(() -> new InvitationTokenNotFound(invitationToken));

		if (invitation.getExpirationDate().isBefore(java.time.LocalDateTime.now())) {
			throw new InvitationExpired();
		}

		AuthTypes.AuthType registerResponse = registerUserInAccountService.register(
			invitation.getInvitedByAccount(),
			name,
			email,
			password
		);

		invitationRepository.delete(invitation);

		return registerResponse;
	}
}
