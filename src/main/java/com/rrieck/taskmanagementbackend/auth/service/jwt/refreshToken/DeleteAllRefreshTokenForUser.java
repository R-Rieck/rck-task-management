package com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken;

import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import com.rrieck.taskmanagementbackend.user.model.UserId;
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
