package com.rrieck.taskmanagementbackend.auth.service.jwt;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.accountMember.model.AccountMember;
import com.rrieck.taskmanagementbackend.accountMember.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.service.jwt.accessToken.CreateAccessToken;
import com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken.CreateRefreshToken;
import com.rrieck.taskmanagementbackend.auth.service.jwt.refreshToken.DeleteAllRefreshTokenForUser;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
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
