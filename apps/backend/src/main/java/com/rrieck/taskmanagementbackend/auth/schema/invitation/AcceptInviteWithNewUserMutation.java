package com.rrieck.taskmanagementbackend.auth.schema.invitation;

import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.invitation.AcceptInviteWithNewUser;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AcceptInviteWithNewUserMutation {
	private final AcceptInviteWithNewUser acceptInviteWithNewUser;

	@MutationMapping
	public AuthTypes.AuthType acceptInviteWithNewUser(
		@Argument UUID invitationToken,
		@Argument String name,
		@Argument String email,
		@Argument String password
	) {
		return acceptInviteWithNewUser.accept(
			invitationToken,
			name,
			email,
			password
		);
	}
}
