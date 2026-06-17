package com.rrieck.taskmanagementbackend.auth.service.account;

import com.rrieck.taskmanagementbackend.auth.exception.user.UserNotFound;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.IssueNewTokenPairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SwitchAccountService {
	private final UserRepository userRepository;
	private final IssueNewTokenPairService issueNewTokenPairService;

	public AuthTypes.AuthType switchAccount(UserId userId, AccountId accountId) {
		var user = userRepository.getOptById(userId).orElseThrow(() -> new UserNotFound(userId));

		user.setLastUsedAccountId(accountId);

		userRepository.save(user);

		var newTokenPair = issueNewTokenPairService.issue(userId, accountId);

		return AuthTypes.AuthType
			.builder()
			.userId(userId)
			.accountId(accountId)
			.accessToken(newTokenPair.getAccessToken())
			.refreshToken(newTokenPair.getRefreshToken())
			.build();
	}
}
