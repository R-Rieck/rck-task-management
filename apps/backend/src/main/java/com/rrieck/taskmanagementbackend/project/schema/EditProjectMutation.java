package com.rrieck.taskmanagementbackend.project.schema;

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

	@MutationMapping
	public ProjectTypes.ProjectType editProject(@Argument ProjectTypes.EditProjectInput input, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			var updated = editProjectService.edit(
				input.projectId(),
				input.name(),
				input.description(),
				input.icon(),
				input.memberIds()
			);

			return ProjectTypes.ProjectType.from(updated);
		});
	}
}
