package com.rrieck.taskmanagementbackend.auth.schema.invitation;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.invitation.AcceptInviteWithExistingUser;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AcceptInviteWithExistingUserMutation {
	private final AcceptInviteWithExistingUser acceptInviteWithExistingUser;

	@MutationMapping
	public AuthTypes.AuthType acceptInviteWithExistingUser(
		@Argument UUID invitationToken,
		@Argument UserId userId
	) {
		return acceptInviteWithExistingUser.accept(
			invitationToken,
			userId
		);
	}
}
