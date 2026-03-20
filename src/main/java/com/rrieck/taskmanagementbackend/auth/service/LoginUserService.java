package com.rrieck.taskmanagementbackend.auth.service;

import com.rrieck.taskmanagementbackend.auth.dto.request.LoginUserRequest;
import com.rrieck.taskmanagementbackend.auth.dto.response.AuthResponse;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.service.jwt.IssueNewTokenPairService;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService {
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final IssueNewTokenPairService issueNewTokenPairService;

	public AuthResponse login(LoginUserRequest request) {
		Authentication result = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.email(),
				request.password()
			)
		);

		User user = userRepository.findById(UserId.fromString(result.getName())).orElseThrow();
		TokenPair tokens = issueNewTokenPairService.issue(user.getId());

		return AuthResponse.builder()
		                   .userId(user.getId())
		                   .accessToken(tokens.getAccessToken())
		                   .refreshToken(tokens.getRefreshToken())
		                   .build();
	}
}
