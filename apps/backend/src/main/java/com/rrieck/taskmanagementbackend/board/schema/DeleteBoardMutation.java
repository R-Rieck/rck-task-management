package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.service.DeleteBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DeleteBoardMutation {
	private final DeleteBoardService deleteBoardService;

	@MutationMapping
	public Boolean deleteBoard(@Argument BoardId boardId, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			deleteBoardService.delete(boardId);
			return true;
		});
	}
}
