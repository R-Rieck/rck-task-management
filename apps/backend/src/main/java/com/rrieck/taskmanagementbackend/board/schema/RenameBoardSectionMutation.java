package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.service.authentication.AuthorizationWrapper;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.board.service.RenameBoardSectionService;
import com.rrieck.taskmanagementbackend.board.repository.BoardSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RenameBoardSectionMutation {
	private final RenameBoardSectionService renameBoardSectionService;
	private final BoardSectionRepository boardSectionRepository;

	@MutationMapping
	public BoardTypes.BoardSectionType renameBoardSection(@Argument BoardSectionId sectionId, @Argument String name, Authentication auth) {
		return AuthorizationWrapper.authenticated(auth, ctx -> {
			renameBoardSectionService.rename(sectionId, name);
			return BoardTypes.BoardSectionType.from(boardSectionRepository.findById(sectionId).orElseThrow());
		});
	}
}
