package com.rrieck.taskmanagementbackend.auth.service.jwt;

import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.service.jwt.accessToken.CreateAccessToken;
import com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken.CreateRefreshToken;
import com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken.RevokeRefreshToken;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueRefreshTokenPairService {
	private final CreateAccessToken createAccessToken;
	private final CreateRefreshToken createRefreshToken;
	private final RevokeRefreshToken revokeRefreshToken;
	private final UserRepository userRepository;

	public TokenPair issue(
		UUID userId,
		RefreshToken refreshToken
	) {
		User user = userRepository.findById(userId).orElseThrow();

		revokeRefreshToken.revoke(refreshToken);

		String newAccessToken = createAccessToken.create(user.getId(), user.getRole());
		RefreshToken newRefreshToken = createRefreshToken.create(user.getId());

		return TokenPair.builder()
		                .accessToken(newAccessToken)
		                .refreshToken(newRefreshToken.getToken())
		                .build();
	}
}
