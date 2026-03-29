package com.rrieck.taskmanagementbackend.accountMemeber.model;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "account_members")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountMember {
	@EmbeddedId
	private AccountMemberId id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_member_user"))
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_member_account"))
	private Account account;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
}
