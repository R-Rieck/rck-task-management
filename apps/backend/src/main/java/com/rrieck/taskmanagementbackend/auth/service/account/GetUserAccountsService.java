package com.rrieck.taskmanagementbackend.auth.service.account;

import com.rrieck.taskmanagementbackend.auth.exception.user.UserNotFound;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.schema.account.AccountTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserAccountsService {
	private final UserRepository userRepository;
	private final AccountMemberRepository accountMemberRepository;

	public List<AccountTypes.AccountType> get(UserId userId) {
		var userOpt = userRepository.getOptById(userId);

		if (userOpt.isEmpty()) throw new UserNotFound(userId);

		var accountMembers = accountMemberRepository.getAllByUserId(userId);

		return accountMembers.stream()
		                     .map(member ->
			                     AccountTypes.AccountType
				                     .builder()
				                     .id(member.getAccount().getId())
				                     .name(member.getAccount().getName())
				                     .build()
		                     )
		                     .toList();
	}
}
