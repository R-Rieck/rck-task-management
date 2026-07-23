package com.rrieck.taskmanagementbackend.project.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.project.service.CreateProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CreateProjectMutation {
	private final CreateProjectService createProjectService;

	@MutationMapping
	public ProjectTypes.ProjectType createProject(@Argument ProjectTypes.CreateProjectInput input, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			var project = createProjectService.create(
				input.name(),
				input.description(),
				input.icon(),
				ctx.accountId(),
				ctx.userId(),
				input.memberIds()
			);
			return ProjectTypes.ProjectType.from(project);
		});
	}
}
