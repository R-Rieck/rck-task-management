package com.rrieck.taskmanagementbackend.auth.service.user;

import com.rrieck.taskmanagementbackend.auth.exception.EmailAlreadyRegistered;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.schema.user.UserTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EditUserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserTypes.UserType edit(
		UserId userId,
		Optional<String> nameOpt,
		Optional<String> emailOpt,
		Optional<String> passwordOpt
	) {
		User user = userRepository.getReferenceById(userId);

		boolean isEmailUsedByAnotherUser = emailOpt
			.flatMap(userRepository::findOptByEmail)
			.map(existingUser -> !existingUser.getId().equals(userId))
			.orElse(false);

		if (isEmailUsedByAnotherUser) throw new EmailAlreadyRegistered();

		emailOpt.ifPresent(user::setEmail);
		nameOpt.ifPresent(user::setName);
		passwordOpt.ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));

		userRepository.save(user);

		return UserTypes.UserType.from(user);
	}
}
