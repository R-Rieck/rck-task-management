package com.rrieck.taskmanagementbackend.auth.service;

import com.rrieck.taskmanagementbackend.auth.dto.request.LoginUserRequest;
import com.rrieck.taskmanagementbackend.auth.dto.response.AuthResponse;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.service.jwt.IssueNewTokenPairService;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.service.GetUserByEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginUserService {
	private final GetUserByEmail getUserByEmail;
	private final AuthenticationManager authenticationManager;
	private final IssueNewTokenPairService issueNewTokenPairService;

	public AuthResponse login(LoginUserRequest request) {
		Authentication result = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.email(),
				request.password()
			)
		);

		User user = getUserByEmail.get(UUID.fromString(result.getName()));
		TokenPair tokens = issueNewTokenPairService.issue(user.getId());

		return AuthResponse.builder()
		                   .userId(user.getId())
		                   .accessToken(tokens.getAccessToken())
		                   .refreshToken(tokens.getRefreshToken())
		                   .build();
	}
}
