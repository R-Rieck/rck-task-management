package com.rrieck.taskmanagementbackend.auth.schema.invitation;

import com.rrieck.taskmanagementbackend.auth.model.invitation.InvitationId;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.auth.service.invitation.RemoveInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RemoveInvitationMutation {
	private final RemoveInvitationService removeInvitationService;

	@MutationMapping
	public Boolean removeInvitation(@Argument InvitationId invitationId, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx ->
			removeInvitationService.remove(ctx.accountId(), ctx.userId(), invitationId)
		);
	}
}
