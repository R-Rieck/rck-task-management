package com.rrieck.taskmanagementbackend.auth.service.jwt.accessToken;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.jwt.JwtClaimKeys;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenType;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtProperties;
import com.rrieck.taskmanagementbackend.auth.service.jwt.token.JwtTokenProvider;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateAccessToken {
	private final JwtProperties jwtProperties;
	private final JwtTokenProvider jwtTokenProvider;

	public String create(
		UserId userId,
		Role role
	) {
		return jwtTokenProvider.generateToken(
			userId.id().toString(),
			jwtProperties.getAccessToken().getSecret(),
			jwtProperties.getAccessToken().getExpiration(),
			Map.of(
				JwtClaimKeys.TYPE, TokenType.ACCESS.name(),
				JwtClaimKeys.ROLE, role.name()
			)
		);
	}
}
