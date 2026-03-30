package com.rrieck.taskmanagementbackend.invitation.schema;

import com.rrieck.taskmanagementbackend.auth.service.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.invitation.service.InviteToAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InviteToAccountMutation {
	private final InviteToAccountService inviteToAccountService;

	@MutationMapping
	public InvitationTypes.InvitationType invite(@Argument List<String> emails, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx ->
			inviteToAccountService.invite(ctx.accountId(), ctx.userId(), emails)
		);
	}
}
