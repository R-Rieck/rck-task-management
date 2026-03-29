package com.rrieck.taskmanagementbackend.user.schema;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTypes {

	public record EditUserInput(Optional<String> name, Optional<String> email, Optional<String> password) {
	}
}
