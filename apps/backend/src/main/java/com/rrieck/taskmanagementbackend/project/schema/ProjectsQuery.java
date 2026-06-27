package com.rrieck.taskmanagementbackend.project.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.project.service.GetUserProjectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectsQuery {
	private final GetUserProjectsService getUserProjectsService;

	@QueryMapping
	public List<ProjectTypes.ProjectType> projects(Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx ->
			getUserProjectsService.getProjects(ctx.accountId(), ctx.userId())
				.stream()
				.map(ProjectTypes.ProjectType::from)
				.toList()
		);
	}
}
