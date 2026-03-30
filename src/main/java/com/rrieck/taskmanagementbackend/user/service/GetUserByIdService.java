package com.rrieck.taskmanagementbackend.user.service;

import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import com.rrieck.taskmanagementbackend.user.schema.UserTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdService {
	private final UserRepository userRepository;

	public UserTypes.UserResponse get(UserId userId) {
		User user = userRepository
			.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		return UserTypes.UserResponse.from(user);
	}
}
