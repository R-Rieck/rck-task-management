package com.rrieck.taskmanagementbackend.board.schema;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.schema.user.UserTypes;
import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.model.BoardMember;
import com.rrieck.taskmanagementbackend.board.model.BoardMemberId;
import com.rrieck.taskmanagementbackend.board.model.BoardSection;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class BoardTypes {
	@Builder
	public record BoardMemberType(
		BoardMemberId id,
		UserTypes.UserType user,
		LocalDateTime joinedAt
	) {
		public static BoardMemberType from(BoardMember member) {
			return BoardMemberType.builder()
			                      .id(member.getId())
			                      .user(UserTypes.UserType.from(member.getUser()))
			                      .joinedAt(member.getJoinedAt())
			                      .build();
		}
	}

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
		UserId ownerId,
		List<BoardSectionType> sections,
		List<BoardMemberType> members,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		public static BoardType from(Board board) {
			return BoardType.builder()
			                .id(board.getId())
			                .name(board.getName())
			                .projectId(board.getProjectId())
			                .ownerId(board.getOwnerId())
			                .sections(board.getSections().stream().map(BoardSectionType::from).toList())
			                .members(board.getMembers().stream().map(BoardMemberType::from).toList())
			                .createdAt(board.getCreatedAt())
			                .updatedAt(board.getUpdatedAt())
			                .build();
		}
	}

	public record CreateBoardInput(
		ProjectId projectId,
		String name,
		List<String> sections,
		List<UserId> memberIds
	) {}

	public record EditBoardInput(
		BoardId boardId,
		String name,
		List<UserId> memberIds
	) {}

	public record CreateBoardSectionInput(
		BoardId boardId,
		String name,
		Integer position
	) {}
}
