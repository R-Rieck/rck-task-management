package com.rrieck.taskmanagementbackend.project.schema;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.service.DeleteProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DeleteProjectMutation {
	private final DeleteProjectService deleteProjectService;

	@MutationMapping
	public Boolean deleteProject(@Argument ProjectId projectId, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			deleteProjectService.delete(projectId);
			return true;
		});
	}
}
