package com.rrieck.taskmanagementbackend.board.repository;

import com.rrieck.taskmanagementbackend.board.model.BoardMember;
import com.rrieck.taskmanagementbackend.board.model.BoardMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, BoardMemberId> {
}
