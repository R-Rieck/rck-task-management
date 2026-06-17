package com.rrieck.taskmanagementbackend.auth.service.user;

import com.rrieck.taskmanagementbackend.auth.exception.EmailAlreadyRegistered;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.email.service.SanitizeEmail;
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
		String sanitizedEmail = SanitizeEmail.sanitize(email);
		boolean doesExist = userRepository.existsByEmail(sanitizedEmail);

		if (doesExist) {
			throw new EmailAlreadyRegistered();
		}

		UserId userId = UserId.generateId();

		User user = User
			.builder()
			.name(name)
			.id(userId)
			.email(sanitizedEmail)
			.password(passwordEncoder.encode(password))
			.registeredAt(LocalDateTime.now())
			.lastUsedAccountId(accountId)
			.build();

		userRepository.save(user);

		return userId;
	}
}
