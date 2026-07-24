package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.board.service.EditBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EditBoardMutation {
	private final EditBoardService editBoardService;

	@MutationMapping
	public BoardTypes.BoardType editBoard(@Argument BoardTypes.EditBoardInput input, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			var board = editBoardService.edit(
				input.boardId(),
				input.name(),
				input.memberIds(),
				ctx.userId()
			);
			return BoardTypes.BoardType.from(board);
		});
	}
}
