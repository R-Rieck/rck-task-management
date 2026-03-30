package com.rrieck.taskmanagementbackend.user.schema;

import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTypes {
	public record EditUserInput(Optional<String> name, Optional<String> email, Optional<String> password) {
	}

	@Builder
	public record UserResponse(
		UserId id,
		String name,
		String email
	) {
		public static UserResponse from(User user) {
			return UserResponse
				.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.build();
		}
	}
}
