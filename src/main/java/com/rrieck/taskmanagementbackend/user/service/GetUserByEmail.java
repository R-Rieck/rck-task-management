package com.rrieck.taskmanagementbackend.user.service;

import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserByEmail {
	private final UserRepository userRepository;

	public User get(UUID userId) {
		return userRepository
			.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}
