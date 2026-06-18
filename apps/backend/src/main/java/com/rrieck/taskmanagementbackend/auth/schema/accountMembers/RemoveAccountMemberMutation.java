package com.rrieck.taskmanagementbackend.auth.schema.accountMembers;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.service.accountMember.RemoveAccountMemberService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RemoveAccountMemberMutation {
	private final RemoveAccountMemberService removeAccountMemberService;

	@MutationMapping
	public Boolean removeAccountMember(Authentication authentication, @Argument UserId userId) {
		return AuthorizationWrapper.authenticated(authentication, ctx ->
			removeAccountMemberService.remove(ctx.accountId(), userId, ctx.userId())
		);
	}
}
