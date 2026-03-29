package com.rrieck.taskmanagementbackend.auth.service;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.account.service.CreateAccountService;
import com.rrieck.taskmanagementbackend.accountMemeber.service.CreateAccountMember;
import com.rrieck.taskmanagementbackend.auth.dto.response.AuthResponse;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.service.jwt.IssueNewTokenPairService;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService {
	private final CreateUserService createUserService;
	private final CreateAccountService createAccountService;
	private final CreateAccountMember createAccountMember;
	private final IssueNewTokenPairService issueNewTokenPairService;

	public AuthResponse register(
		String name,
		String email,
		String password
	) {
		AccountId accountId = createAccountService.create(name);
		UserId userId = createUserService.create(
			accountId,
			email,
			password,
			name
		);

		createAccountMember.create(accountId, userId, Role.Admin);

		TokenPair tokens = issueNewTokenPairService.issue(userId, accountId);

		return AuthResponse
			.builder()
			.userId(userId)
			.accountId(accountId)
			.accessToken(tokens.getAccessToken())
			.refreshToken(tokens.getRefreshToken())
			.build();
	}
}
