package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.model.BoardSection;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class BoardTypes {
	@Builder
	public record BoardSectionType(
		BoardSectionId id,
		String name,
		int position
	) {
		public static BoardSectionType from(BoardSection section) {
			return BoardSectionType.builder()
			                        .id(section.getId())
			                        .name(section.getName())
			                        .position(section.getPosition())
			                        .build();
		}
	}

	@Builder
	public record BoardType(
		BoardId id,
		String name,
		ProjectId projectId,
		List<BoardSectionType> sections,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		public static BoardType from(Board board) {
			return BoardType.builder()
			                .id(board.getId())
			                .name(board.getName())
			                .projectId(board.getProjectId())
			                .sections(board.getSections().stream().map(BoardSectionType::from).toList())
			                .createdAt(board.getCreatedAt())
			                .updatedAt(board.getUpdatedAt())
			                .build();
		}
	}

	public record CreateBoardInput(
		ProjectId projectId,
		String name,
		List<String> sections
	) {}
}
