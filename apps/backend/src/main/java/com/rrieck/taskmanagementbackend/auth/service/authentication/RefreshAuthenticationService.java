package com.rrieck.taskmanagementbackend.auth.service.authentication;

import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.IssueRefreshTokenPairService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.refreshToken.CheckRefreshTokenForValidity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshAuthenticationService {
	private final IssueRefreshTokenPairService issueRefreshTokenPairService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final CheckRefreshTokenForValidity checkRefreshTokenForValidity;

	public AuthTypes.AuthType refresh(String refreshToken) {
		RefreshToken existingRefreshToken = refreshTokenRepository.findByToken(refreshToken).orElseThrow();
		User user = existingRefreshToken.getUser();

		if (existingRefreshToken.isRevoked()) {
			throw new IllegalArgumentException("Refresh token is revoked");
		}

		if (existingRefreshToken.getExpiration().isBefore(java.time.LocalDateTime.now())) {
			throw new IllegalArgumentException("Refresh token is expired");
		}

		if (!checkRefreshTokenForValidity.isValid(refreshToken, user.getId().id().toString())) {
			throw new IllegalArgumentException("Invalid refresh token");
		}

		TokenPair newTokens = issueRefreshTokenPairService.issue(user.getId(), existingRefreshToken);

		return AuthTypes.AuthType
			.builder()
			.accessToken(newTokens.getAccessToken())
			.refreshToken(newTokens.getRefreshToken())
			.accountId(user.getLastUsedAccountId())
			.userId(user.getId())
			.build();
	}
}
