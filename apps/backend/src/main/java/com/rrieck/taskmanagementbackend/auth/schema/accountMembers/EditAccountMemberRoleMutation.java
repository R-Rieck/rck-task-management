package com.rrieck.taskmanagementbackend.auth.schema.accountMembers;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.service.accountMember.EditMemberRoleService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EditAccountMemberRoleMutation {
	private final EditMemberRoleService editMemberRoleService;

	@MutationMapping
	public AccountMemberTypes.AccountMemberType editAccountMemberRole(
		Authentication authentication,
		@Argument UserId userId,
		@Argument Role role
	) {
		return AuthorizationWrapper.authenticated(authentication, ctx ->
			editMemberRoleService.EditMemberRoleService(
				ctx.accountId(),
				userId,
				role
			)
		);
	}
}
