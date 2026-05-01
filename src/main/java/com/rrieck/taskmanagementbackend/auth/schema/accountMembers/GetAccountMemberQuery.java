package com.rrieck.taskmanagementbackend.auth.schema.accountMembers;

import com.rrieck.taskmanagementbackend.auth.service.accountMember.GetAccountMember;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GetAccountMemberQuery {
	private final GetAccountMember getAccountMember;

	@QueryMapping
	public AccountMemberTypes.AccountMemberWithInvitationType getMembers(
		Authentication authentication
	) {
		return AuthorizationWrapper.authenticated(authentication, ctx ->
			getAccountMember.get(ctx.accountId())
		);
	}
}
