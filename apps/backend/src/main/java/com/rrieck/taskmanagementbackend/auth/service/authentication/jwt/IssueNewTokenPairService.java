package com.rrieck.taskmanagementbackend.auth.service.authentication.jwt;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.accessToken.CreateAccessToken;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.refreshToken.CreateRefreshToken;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.refreshToken.DeleteAllRefreshTokenForUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueNewTokenPairService {
	private final CreateRefreshToken createRefreshToken;
	private final CreateAccessToken createAccessToken;
	private final DeleteAllRefreshTokenForUser deleteAllRefreshTokenForUser;
	private final UserRepository userRepository;
	private final AccountMemberRepository accountMemberRepository;

	@Transactional
	public TokenPair issue(UserId userId, AccountId accountId) {
		User user = userRepository.findById(userId).orElseThrow();
		AccountMember accountMember = accountMemberRepository.getByAccountIdAndUserId(accountId, userId);

		deleteAllRefreshTokenForUser.delete(user.getId());

		String accessToken = createAccessToken.create(
			user.getEmail(),
			user.getId(),
			user.getLastUsedAccountId(),
			accountMember.getRole()
		);
		RefreshToken refreshToken = createRefreshToken.create(user.getId());

		return TokenPair.builder()
		                .accessToken(accessToken)
		                .refreshToken(refreshToken.getToken())
		                .build();
	}
}
