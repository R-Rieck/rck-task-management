package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.board.service.DeleteBoardSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DeleteBoardSectionMutation {
	private final DeleteBoardSectionService deleteBoardSectionService;

	@MutationMapping
	public Boolean deleteBoardSection(@Argument BoardSectionId sectionId, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			deleteBoardSectionService.delete(sectionId);
			return true;
		});
	}
}
