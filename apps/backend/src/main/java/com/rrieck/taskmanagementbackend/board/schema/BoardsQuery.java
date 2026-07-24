package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.service.GetBoardsService;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardsQuery {
	private final GetBoardsService getBoardsService;

	@QueryMapping
	public List<BoardTypes.BoardType> boards(@Argument ProjectId projectId, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx ->
			getBoardsService.getBoards(projectId, ctx.userId())
				.stream().map(BoardTypes.BoardType::from).toList()
		);
	}

	@QueryMapping
	public BoardTypes.BoardType board(@Argument BoardId id, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx ->
			BoardTypes.BoardType.from(getBoardsService.getBoard(id, ctx.userId()))
		);
	}
}
