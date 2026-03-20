package com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken;

import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAllRefreshTokenForUser {
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserRepository userRepository;

	public void delete(UserId userId) {
		refreshTokenRepository.deleteByUserId(userId);
	}
}
