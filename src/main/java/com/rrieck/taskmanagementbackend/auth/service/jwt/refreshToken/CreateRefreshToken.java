package com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken;

import com.rrieck.taskmanagementbackend.auth.model.jwt.JwtClaimKeys;
import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenType;
import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtProperties;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtTokenProvider;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CreateRefreshToken {
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtProperties jwtProperties;
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserRepository userRepository;

	@Value("${security.jwt.refresh-token.expiration}")
	private long expirationMs;

	public RefreshToken create(UUID userId) {
		User user = userRepository.findById(userId).orElseThrow();

		String token = jwtTokenProvider.generateToken(
			user.getId().toString(),
			jwtProperties.getRefreshToken().getSecret(),
			jwtProperties.getRefreshToken().getExpiration(),
			Map.of(JwtClaimKeys.TYPE, TokenType.REFRESH.name())

		);

		RefreshToken refreshToken =
			RefreshToken.builder()
			            .token(token)
			            .user(user)
			            .expiration(LocalDateTime.now().plusSeconds(expirationMs / 1000))
			            .revoked(false)
			            .build();

		return refreshTokenRepository.save(refreshToken);
	}
}
