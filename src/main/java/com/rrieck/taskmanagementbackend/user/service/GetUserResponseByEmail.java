package com.rrieck.taskmanagementbackend.user.service;

import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserResponseByEmail {
	private final UserRepository userRepository;

	public UserResponse get(String userId) {
		User user = userRepository
			.findById(UUID.fromString(userId))
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		return UserResponse
			.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.role(user.getRole().name())
			.build();
	}
}
