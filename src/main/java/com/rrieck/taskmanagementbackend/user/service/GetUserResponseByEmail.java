package com.rrieck.taskmanagementbackend.user.service;

import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserResponseByEmail {
	private final UserRepository userRepository;

	public UserResponse get(UserId userId) {
		User user = userRepository
			.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		return UserResponse.from(user);
	}
}
