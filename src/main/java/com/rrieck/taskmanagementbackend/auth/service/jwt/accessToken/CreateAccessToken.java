package com.rrieck.taskmanagementbackend.auth.service.jwt.accessToken;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
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
		String email,
		UserId userId,
		AccountId accountId,
		Role role
	) {
		return jwtTokenProvider.generateToken(
			email,
			jwtProperties.getAccessToken().getSecret(),
			jwtProperties.getAccessToken().getExpiration(),
			Map.of(
				JwtClaimKeys.TYPE, TokenType.ACCESS.name(),
				JwtClaimKeys.ROLE, role.name(),
				JwtClaimKeys.ACCOUNT_ID, accountId,
				JwtClaimKeys.USER_ID, userId
			)
		);
	}
}
