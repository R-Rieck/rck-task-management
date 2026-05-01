package com.rrieck.taskmanagementbackend.auth.schema.user;

import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTypes {
	@Builder
	public record UserType(
		UserId id,
		String name,
		String email
	) {
		public static UserType from(User user) {
			return UserType
				.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.build();
		}
	}

	public record EditUserInput(Optional<String> name, Optional<String> email, Optional<String> password) {
	}
}
