package com.rrieck.taskmanagementbackend.auth.service.userDetails;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMember;
import com.rrieck.taskmanagementbackend.accountMemeber.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {
	@Test
	void loadUserByUsernameUsesUserIdAsPrincipalName() {
		UserRepository userRepository = mock(UserRepository.class);
		AccountMemberRepository accountMemberRepository = mock(AccountMemberRepository.class);
		CustomUserDetailsService service = new CustomUserDetailsService(userRepository, accountMemberRepository);

		UserId userId = UserId.generateId();
		AccountId accountId = AccountId.generateId();
		User user = User.builder()
		                .id(userId)
		                .name("Test User")
		                .email("user@example.com")
		                .password("encoded-password")
		                .registeredAt(LocalDateTime.now())
		                .lastUsedAccountId(accountId)
		                .build();
		AccountMember accountMember = AccountMember.builder()
		                                           .user(user)
		                                           .account(Account.builder().id(accountId).name("Account").build())
		                                           .role(Role.Admin)
		                                           .build();

		when(userRepository.findOptByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(accountMemberRepository.getByAccountIdAndUserId(accountId, userId)).thenReturn(accountMember);

		var userDetails = service.loadUserByUsername(user.getEmail());

		assertEquals(userId.id().toString(), userDetails.getUsername());
	}

	@Test
	void loadUserByIdUsesUserIdAsPrincipalName() {
		UserRepository userRepository = mock(UserRepository.class);
		AccountMemberRepository accountMemberRepository = mock(AccountMemberRepository.class);
		CustomUserDetailsService service = new CustomUserDetailsService(userRepository, accountMemberRepository);

		UserId userId = UserId.generateId();
		AccountId accountId = AccountId.generateId();
		User user = User.builder()
		                .id(userId)
		                .name("Test User")
		                .email("user@example.com")
		                .password("encoded-password")
		                .registeredAt(LocalDateTime.now())
		                .lastUsedAccountId(accountId)
		                .build();
		AccountMember accountMember = AccountMember.builder()
		                                           .user(user)
		                                           .account(Account.builder().id(accountId).name("Account").build())
		                                           .role(Role.Admin)
		                                           .build();

		when(userRepository.findById(argThat(id -> id.id().equals(userId.id())))).thenReturn(Optional.of(user));
		when(accountMemberRepository.getByAccountIdAndUserId(accountId, userId)).thenReturn(accountMember);

		var userDetails = service.loadUserById(userId.id().toString());

		assertEquals(userId.id().toString(), userDetails.getUsername());
	}
}
