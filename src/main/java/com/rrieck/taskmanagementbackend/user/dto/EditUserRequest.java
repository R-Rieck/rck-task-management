package com.rrieck.taskmanagementbackend.user.dto;

import java.util.Optional;

public record EditUserRequest(
	Optional<String> nameOpt,
	Optional<String> emailOpt,
	Optional<String> passwordOpt
) {
}
