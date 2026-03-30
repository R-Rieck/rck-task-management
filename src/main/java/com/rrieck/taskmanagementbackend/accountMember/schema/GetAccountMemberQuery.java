package com.rrieck.taskmanagementbackend.accountMember.schema;

import com.rrieck.taskmanagementbackend.accountMember.service.GetAccountMember;
import com.rrieck.taskmanagementbackend.auth.service.AuthorizationWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GetAccountMemberQuery {
	private final GetAccountMember getAccountMember;

	@QueryMapping
	public AccountMemberTypes.AccountMemberResponse getMembers(
		Authentication authentication
	) {
		return AuthorizationWrapper.authenticated(authentication, ctx ->
			getAccountMember.get(ctx.accountId())
		);
	}
}
