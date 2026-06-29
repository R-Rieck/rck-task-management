package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.board.service.GetBoardsService;
import com.rrieck.taskmanagementbackend.project.schema.ProjectTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectBoardResolver {
	private final GetBoardsService getBoardsService;

	@SchemaMapping(typeName = "ProjectType", field = "boards")
	public List<BoardTypes.BoardType> boards(ProjectTypes.ProjectType project) {
		return getBoardsService.getBoards(project.id()).stream()
			.map(BoardTypes.BoardType::from)
			.toList();
	}
}
