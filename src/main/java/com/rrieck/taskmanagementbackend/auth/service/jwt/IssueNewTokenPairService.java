package com.rrieck.taskmanagementbackend.auth.service.jwt;

import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.service.jwt.accessToken.CreateAccessToken;
import com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken.CreateRefreshToken;
import com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken.DeleteAllRefreshTokenForUser;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueNewTokenPairService {
	private final CreateRefreshToken createRefreshToken;
	private final CreateAccessToken createAccessToken;
	private final DeleteAllRefreshTokenForUser deleteAllRefreshTokenForUser;
	private final UserRepository userRepository;

	public TokenPair issue(UUID userId) {
		User user = userRepository.findById(userId).orElseThrow();

		deleteAllRefreshTokenForUser.delete(user.getId());

		String accessToken = createAccessToken.create(user.getId(), user.getRole());
		RefreshToken refreshToken = createRefreshToken.create(user.getId());

		return TokenPair.builder()
		                .accessToken(accessToken)
		                .refreshToken(refreshToken.getToken())
		                .build();
	}
}
