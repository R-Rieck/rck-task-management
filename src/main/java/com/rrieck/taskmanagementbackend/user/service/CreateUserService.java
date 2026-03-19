package com.rrieck.taskmanagementbackend.user.service;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User create(
		Boolean isAdmin,
		String email,
		String password,
		String name
	) {
		boolean doesExist = userRepository.existsByEmail(email);

		if (doesExist) {
			throw new IllegalStateException("Email already in use");
		}

		Role role = isAdmin ?
			Role.Admin :
			Role.User;

		User user = User
			.builder()
			.name(name)
			.email(email)
			.password(passwordEncoder.encode(password))
			.role(role)
			.build();

		return userRepository.save(user);
	}
}
