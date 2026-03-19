package com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken;

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
public class CheckRefreshTokenForValidity {
	private final JwtProperties jwtProperties;
	private final JwtTokenProvider jwtTokenProvider;

	public boolean isValid(String token, String expectedSubject) {
		Claims claims = jwtTokenProvider.extractClaims(token, jwtProperties.getRefreshToken().getSecret());

		String type = claims.get(JwtClaimKeys.TYPE, String.class);

		return TokenType.REFRESH.equals(type)
			&& claims.getSubject().equals(expectedSubject)
			&& claims.getExpiration().after(new Date());
	}
}
