package com.rrieck.taskmanagementbackend.accountMemeber.controller;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.accountMemeber.dto.response.AccountMemberResponse;
import com.rrieck.taskmanagementbackend.accountMemeber.service.GetAccountMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account-memeber")
@RequiredArgsConstructor
public class AccountMemberController {
	private final GetAccountMember getAccountMember;

	@GetMapping("{accountId}")
	public AccountMemberResponse get(@PathVariable String accountId, Authentication authentication) {
		return getAccountMember.get(
			AccountId.fromString(accountId)
		);
	}
}
