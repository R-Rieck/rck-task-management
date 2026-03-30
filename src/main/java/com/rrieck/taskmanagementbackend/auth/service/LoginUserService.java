package com.rrieck.taskmanagementbackend.auth.service;

import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.schema.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.jwt.IssueNewTokenPairService;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService {
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final IssueNewTokenPairService issueNewTokenPairService;

	public AuthTypes.AuthResponseType login(String email, String password) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				email,
				password
			)
		);

		User user = userRepository.findOptByEmail(email).orElseThrow();
		TokenPair tokens = issueNewTokenPairService.issue(user.getId(), user.getLastUsedAccountId());

		return AuthTypes.AuthResponseType
			.builder()
			.userId(user.getId())
			.accountId(user.getLastUsedAccountId())
			.accessToken(tokens.getAccessToken())
			.refreshToken(tokens.getRefreshToken())
			.build();
	}
}
