package com.rrieck.taskmanagementbackend.board.model;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Board {
	@EmbeddedId
	private BoardId id;

	@Column(nullable = false)
	private String name;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "owner_user_id", nullable = false))
	private UserId ownerId;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "account_id", nullable = false))
	private AccountId accountId;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "project_id", nullable = false))
	private ProjectId projectId;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@OrderBy("position ASC")
	@Builder.Default
	private List<BoardSection> sections = new ArrayList<>();

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
}
