package com.rrieck.taskmanagementbackend.accountMemeber.dto.response;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import lombok.Builder;

@Builder
public record MemberWithRole(
	UserResponse user,
	Role role
) {
	public static MemberWithRole from(UserResponse user, Role role) {
		return MemberWithRole
			.builder()
			.user(user)
			.role(role)
			.build();
	}
}
