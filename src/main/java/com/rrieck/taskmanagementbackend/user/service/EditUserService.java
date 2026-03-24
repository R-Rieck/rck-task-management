package com.rrieck.taskmanagementbackend.user.service;

import com.rrieck.taskmanagementbackend.user.EmailAlreadyRegistered;
import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EditUserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserResponse edit(
		UserId userId,
		Optional<String> nameOpt,
		Optional<String> emailOpt,
		Optional<String> passwordOpt
	) {
		User user = userRepository.getReferenceById(userId);
		Boolean doesEmailExists = emailOpt.isPresent() ? userRepository.existsByEmail(emailOpt.get()) : false;

		if (doesEmailExists) throw new EmailAlreadyRegistered(emailOpt.get());

		emailOpt.ifPresent(user::setEmail);
		nameOpt.ifPresent(user::setName);
		passwordOpt.ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));

		userRepository.save(user);

		return UserResponse.from(user);
	}
}
