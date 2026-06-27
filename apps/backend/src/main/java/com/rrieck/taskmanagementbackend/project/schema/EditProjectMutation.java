package com.rrieck.taskmanagementbackend.project.schema;

import com.rrieck.taskmanagementbackend.auth.exception.accountMember.AccountMemberNotAdmin;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.project.service.EditProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EditProjectMutation {
	private final EditProjectService editProjectService;
	private final AccountMemberRepository accountMemberRepository;

	@MutationMapping
	public ProjectTypes.ProjectType editProject(@Argument ProjectTypes.EditProjectInput input, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			var requester = accountMemberRepository
				.getOptByAccountIdAndUserId(ctx.accountId(), ctx.userId())
				.orElseThrow();

			if (requester.getRole() != Role.Admin
				&& !ctx.userId().id().equals(input.projectId().id())) {
				throw new AccountMemberNotAdmin();
			}

			var project = editProjectService.edit(
				input.projectId(),
				input.name(),
				input.description(),
				input.isPrivate()
			);
			return ProjectTypes.ProjectType.from(project);
		});
	}
}
