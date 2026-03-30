package com.rrieck.taskmanagementbackend.accountMemeber.dto.response;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.schema.UserTypes;
import lombok.Builder;

@Builder
public record MemberWithRole(
	UserTypes.UserResponse user,
	Role role
) {
	public static MemberWithRole from(UserTypes.UserResponse user, Role role) {
		return MemberWithRole
			.builder()
			.user(user)
			.role(role)
			.build();
	}
}
