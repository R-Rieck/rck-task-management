package com.rrieck.taskmanagementbackend.auth.schema.accountMembers;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMemberId;
import com.rrieck.taskmanagementbackend.auth.model.invitation.Invitation;
import com.rrieck.taskmanagementbackend.auth.schema.account.AccountTypes;
import com.rrieck.taskmanagementbackend.auth.schema.invitation.InvitationTypes;
import com.rrieck.taskmanagementbackend.auth.schema.user.UserTypes;
import lombok.Builder;

import java.util.List;


public class AccountMemberTypes {
	@Builder
	public record AccountMemberType(
		AccountMemberId id,
		UserTypes.UserType user,
		AccountTypes.AccountType account,
		Role role
	) {
		public static AccountMemberType from(AccountMember accountMember) {
			return AccountMemberType
				.builder()
				.id(accountMember.getId())
				.user(UserTypes.UserType.from(accountMember.getUser()))
				.account(AccountTypes.AccountType.from(accountMember.getAccount()))
				.role(accountMember.getRole())
				.build();
		}
	}

	@Builder
	public record AccountMemberWithInvitationType(
		String accountName,
		List<AccountMemberType> members,
		List<InvitationTypes.InvitationType> openInvitations
	) {
		public static AccountMemberWithInvitationType from(
			String accountName,
			List<AccountMember> members,
			List<Invitation> openInvitations
		) {
			return AccountMemberWithInvitationType
				.builder()
				.accountName(accountName)
				.members(members
					.stream()
					.map(member -> AccountMemberType.from(member))
					.toList()
				)
				.openInvitations(openInvitations
					.stream()
					.map(invitation -> InvitationTypes.InvitationType.from(invitation))
					.toList()
				)
				.build();
		}
	}
}