package com.rrieck.taskmanagementbackend.auth.service.authentication;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.account.CreateAccountService;
import com.rrieck.taskmanagementbackend.auth.service.accountMember.CreateAccountMember;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.IssueNewTokenPairService;
import com.rrieck.taskmanagementbackend.auth.service.user.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserInAccountService {
	private final CreateUserService createUserService;
	private final CreateAccountService createAccountService;
	private final CreateAccountMember createAccountMember;
	private final IssueNewTokenPairService issueNewTokenPairService;

	public AuthTypes.AuthType register(
		AccountId accountId,
		String name,
		String email,
		String password
	) {
		UserId userId = createUserService.create(
			accountId,
			email,
			password,
			name
		);

		createAccountMember.create(accountId, userId, Role.Admin);

		TokenPair tokens = issueNewTokenPairService.issue(userId, accountId);

		return AuthTypes.AuthType
			.builder()
			.userId(userId)
			.accountId(accountId)
			.accessToken(tokens.getAccessToken())
			.refreshToken(tokens.getRefreshToken())
			.build();
	}
}
