package com.rrieck.taskmanagementbackend.board.repository;

import com.rrieck.taskmanagementbackend.board.model.BoardSection;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardSectionRepository extends JpaRepository<BoardSection, BoardSectionId> {
}
