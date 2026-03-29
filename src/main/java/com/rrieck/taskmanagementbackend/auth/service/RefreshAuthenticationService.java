package com.rrieck.taskmanagementbackend.auth.service;

import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import com.rrieck.taskmanagementbackend.auth.schema.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.jwt.IssueRefreshTokenPairService;
import com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken.CheckRefreshTokenForValidity;
import com.rrieck.taskmanagementbackend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshAuthenticationService {
	private final IssueRefreshTokenPairService issueRefreshTokenPairService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final CheckRefreshTokenForValidity checkRefreshTokenForValidity;

	public AuthTypes.AuthResponseType refresh(String refreshToken) {
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

		return AuthTypes.AuthResponseType
			.builder()
			.accessToken(newTokens.getAccessToken())
			.refreshToken(newTokens.getRefreshToken())
			.accountId(user.getLastUsedAccountId())
			.userId(user.getId())
			.build();
	}
}
