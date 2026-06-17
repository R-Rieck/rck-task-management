package com.rrieck.taskmanagementbackend.auth.service.authentication;

import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.IssueNewTokenPairService;
import com.rrieck.taskmanagementbackend.email.service.SanitizeEmail;
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

	public AuthTypes.AuthType login(String email, String password) {
		String sanitizedEmail = SanitizeEmail.sanitize(email);
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				sanitizedEmail,
				password
			)
		);

		User user = userRepository.findOptByEmail(sanitizedEmail).orElseThrow();
		TokenPair tokens = issueNewTokenPairService.issue(user.getId(), user.getLastUsedAccountId());

		return AuthTypes.AuthType
			.builder()
			.userId(user.getId())
			.accountId(user.getLastUsedAccountId())
			.accessToken(tokens.getAccessToken())
			.refreshToken(tokens.getRefreshToken())
			.build();
	}
}
