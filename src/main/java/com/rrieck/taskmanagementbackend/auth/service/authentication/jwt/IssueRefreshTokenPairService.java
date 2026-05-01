package com.rrieck.taskmanagementbackend.auth.service.authentication.jwt;

import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.accessToken.CreateAccessToken;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.refreshToken.CreateRefreshToken;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.refreshToken.RevokeRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueRefreshTokenPairService {
	private final CreateAccessToken createAccessToken;
	private final CreateRefreshToken createRefreshToken;
	private final RevokeRefreshToken revokeRefreshToken;
	private final UserRepository userRepository;
	private final AccountMemberRepository accountMemberRepository;

	public TokenPair issue(
		UserId userId,
		RefreshToken refreshToken
	) {
		User user = userRepository.findById(userId).orElseThrow();
		AccountMember member = accountMemberRepository.getByAccountIdAndUserId(user.getLastUsedAccountId(), user.getId());

		revokeRefreshToken.revoke(refreshToken);

		String newAccessToken = createAccessToken.create(
			user.getEmail(),
			user.getId(),
			user.getLastUsedAccountId(),
			member.getRole()
		);
		RefreshToken newRefreshToken = createRefreshToken.create(user.getId());

		return TokenPair.builder()
		                .accessToken(newAccessToken)
		                .refreshToken(newRefreshToken.getToken())
		                .build();
	}
}
