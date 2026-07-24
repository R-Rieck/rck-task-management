package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.model.AuthorizationContext;
import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.board.service.GetBoardsService;
import com.rrieck.taskmanagementbackend.project.schema.ProjectTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectBoardResolver {
	private final GetBoardsService getBoardsService;

	@SchemaMapping(typeName = "ProjectType", field = "boards")
	public List<BoardTypes.BoardType> boards(ProjectTypes.ProjectType project) {
		var userId = AuthorizationWrapper.maybeAuthenticated(
			SecurityContextHolder.getContext().getAuthentication(),
			optCtx -> optCtx.map(AuthorizationContext::userId).orElse(null)
		);
		if (userId == null) return List.of();

		return getBoardsService.getBoards(project.id(), userId).stream()
			.map(BoardTypes.BoardType::from)
			.toList();
	}
}
