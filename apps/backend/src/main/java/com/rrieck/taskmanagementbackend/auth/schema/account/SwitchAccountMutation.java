package com.rrieck.taskmanagementbackend.auth.schema.account;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.account.SwitchAccountService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SwitchAccountMutation {
	private final SwitchAccountService switchAccountService;

	@MutationMapping
	public AuthTypes.AuthType switchAccount(@Argument AccountId toAccountId, Authentication authentication) {
		return AuthorizationWrapper.authenticated(authentication, ctx ->
			switchAccountService.switchAccount(
				ctx.userId(),
				toAccountId
			)
		);
	}
}
