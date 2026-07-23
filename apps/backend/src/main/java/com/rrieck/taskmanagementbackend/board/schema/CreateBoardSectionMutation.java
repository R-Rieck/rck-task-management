package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.board.service.CreateBoardSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CreateBoardSectionMutation {
	private final CreateBoardSectionService createBoardSectionService;

	@MutationMapping
	public BoardTypes.BoardSectionType createBoardSection(@Argument BoardTypes.CreateBoardSectionInput input, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			var section = createBoardSectionService.create(
				input.boardId(),
				input.name(),
				input.position()
			);
			return BoardTypes.BoardSectionType.from(section);
		});
	}
}
