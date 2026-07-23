package com.rrieck.taskmanagementbackend.project.model;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMember;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Project {
	@EmbeddedId
	private ProjectId id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = true)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		name = "owner_user_id",
		referencedColumnName = "id",
		nullable = false,
		foreignKey = @ForeignKey(name = "fk_project_user")
	)
	private User owner;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "account_id", nullable = false))
	private AccountId account;

	@Column(nullable = true)
	private String icon;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@Builder.Default
	private List<ProjectMember> members = new ArrayList<>();
}
