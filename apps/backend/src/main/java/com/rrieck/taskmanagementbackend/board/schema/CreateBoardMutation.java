package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.board.service.CreateBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CreateBoardMutation {
	private final CreateBoardService createBoardService;

	@MutationMapping
	public BoardTypes.BoardType createBoard(@Argument BoardTypes.CreateBoardInput input, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			var board = createBoardService.create(
				input.name(),
				input.sections(),
				input.projectId(),
				ctx.userId(),
				ctx.accountId()
			);
			return BoardTypes.BoardType.from(board);
		});
	}
}
