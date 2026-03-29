package com.rrieck.taskmanagementbackend.auth.schema;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public class AuthTypes {
	public record LoginUserInput(
		@NotBlank @Email String email,
		@NotBlank String password
	) {
	}

	public record LogoutInput(
		@NotBlank String refreshToken
	) {
	}

	public record RefreshAuthenticationInput(
		@NotBlank String refreshToken
	) {
	}

	public record RegisterUserInput(
		@NotBlank String name,
		@NotBlank @Email String email,
		@NotBlank @Size(min = 8, max = 50) String password
	) {
	}

	@Builder
	public record AuthResponseType(
		String accessToken,
		String refreshToken,
		UserId userId,
		AccountId accountId
	) {
	}

}
