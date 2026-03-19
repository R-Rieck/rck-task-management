package com.rrieck.taskmanagementbackend.auth.service;

import com.rrieck.taskmanagementbackend.auth.dto.response.AuthResponse;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.service.jwt.IssueNewTokenPairService;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService {
	private final CreateUserService createUser;
	private final IssueNewTokenPairService issueNewTokenPairService;

	public AuthResponse register(
		String name,
		String email,
		String password
	) {
		User user = createUser.create(
			false,
			email,
			password,
			name
		);

		TokenPair tokens = issueNewTokenPairService.issue(
			user.getId()
		);

		return AuthResponse.builder()
		                   .userId(user.getId())
		                   .accessToken(tokens.getAccessToken())
		                   .refreshToken(tokens.getRefreshToken())
		                   .build();
	}
}
