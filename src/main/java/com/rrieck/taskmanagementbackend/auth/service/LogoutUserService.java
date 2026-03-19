package com.rrieck.taskmanagementbackend.auth.service;

import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken.RevokeRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutUserService {
	private final RevokeRefreshToken revokeRefreshToken;
	private final RefreshTokenRepository refreshTokenRepository;

	public void logout(String refreshToken) {
		RefreshToken existingRefreshToken = refreshTokenRepository.findByToken(refreshToken).orElseThrow();
		revokeRefreshToken.revoke(existingRefreshToken);
	}
}
