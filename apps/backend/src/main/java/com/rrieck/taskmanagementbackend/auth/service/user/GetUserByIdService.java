package com.rrieck.taskmanagementbackend.auth.service.user;

import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.schema.user.UserTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdService {
	private final UserRepository userRepository;

	public UserTypes.UserType get(UserId userId) {
		User user = userRepository
			.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		return UserTypes.UserType.from(user);
	}
}
