package com.rrieck.taskmanagementbackend.auth.schema.account;

import com.rrieck.taskmanagementbackend.auth.service.account.GetUserAccountsService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GetUserAccountsQuery {
	private final GetUserAccountsService getUserAccountsService;

	@QueryMapping
	public List<AccountTypes.AccountType> getUserAccounts(
		Authentication authentication
	) {
		return AuthorizationWrapper.authenticated(authentication, ctx ->
			getUserAccountsService.get(ctx.userId())
		);
	}
}
