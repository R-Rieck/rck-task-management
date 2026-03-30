package com.rrieck.taskmanagementbackend.user.service;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.user.exception.EmailAlreadyRegistered;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateUserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserId create(
		AccountId accountId,
		String email,
		String password,
		String name
	) {
		boolean doesExist = userRepository.existsByEmail(email);

		if (doesExist) {
			throw new EmailAlreadyRegistered();
		}

		UserId userId = UserId.generateId();

		User user = User
			.builder()
			.name(name)
			.id(userId)
			.email(email.toLowerCase().trim())
			.password(passwordEncoder.encode(password))
			.registeredAt(LocalDateTime.now())
			.lastUsedAccountId(accountId)
			.build();

		userRepository.save(user);

		return userId;
	}
}
