package com.rrieck.taskmanagementbackend.auth.service.jwt.accessToken;

import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenType;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtProperties;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CheckAccessTokenForValidity {
	private final JwtProperties jwtProperties;
	private final JwtTokenProvider jwtTokenProvider;

	public boolean isValid(String token, String expectedSubject) {
		String secret = jwtProperties.getAccessToken().getSecret();
		String type = jwtTokenProvider.extractType(token, secret);
		String email = jwtTokenProvider.extractEmail(token, secret);
		Date expiration = jwtTokenProvider.extractExpiration(token, secret);

		Boolean isValidType = TokenType.ACCESS.name().equals(type);
		Boolean isValidId = email.equals(expectedSubject);
		Boolean isNotExpired = expiration.after(new Date());

		return isValidType && isValidId && isNotExpired;
	}
}
