package com.rrieck.taskmanagementbackend.auth.service.jwt.accessToken;

import com.rrieck.taskmanagementbackend.auth.model.jwt.JwtClaimKeys;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenType;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtProperties;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CheckAccessTokenForValidity {
	private final JwtProperties jwtProperties;
	private final JwtTokenProvider jwtTokenProvider;

	public boolean isValid(String token, String expectedSubject) {
		Claims claims = jwtTokenProvider.extractClaims(token, jwtProperties.getAccessToken().getSecret());

		String type = claims.get(JwtClaimKeys.TYPE, String.class);

		Boolean isValidType = TokenType.ACCESS.name().equals(type);
		Boolean isValidId = claims.getSubject().equals(expectedSubject);
		Boolean isNotExpired = claims.getExpiration().after(new Date());

		return isValidType && isValidId && isNotExpired;
	}
}
