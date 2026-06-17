package com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.refreshToken;

import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenType;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.token.JwtProperties;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CheckRefreshTokenForValidity {
	private final JwtProperties jwtProperties;
	private final JwtTokenProvider jwtTokenProvider;

	public boolean isValid(String token, String expectedSubject) {
		String secret = jwtProperties.getRefreshToken().getSecret();
		String type = jwtTokenProvider.extractType(token, secret);
		String email = jwtTokenProvider.extractEmail(token, secret);
		Date expiration = jwtTokenProvider.extractExpiration(token, secret);

		return TokenType.REFRESH.name().equals(type)
			&& email.equals(expectedSubject)
			&& expiration.after(new Date());
	}
}
