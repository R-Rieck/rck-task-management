package com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.refreshToken;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAllRefreshTokenForUser {
	private final RefreshTokenRepository refreshTokenRepository;

	public void delete(UserId userId) {
		refreshTokenRepository.deleteByUserId(userId);
	}
}
