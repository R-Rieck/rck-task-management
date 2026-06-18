package com.rrieck.taskmanagementbackend.auth.exception.accountMember;

import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import org.springframework.http.HttpStatus;

public class AccountMemberNotAdmin extends OutgoingException {
	public AccountMemberNotAdmin() {
		super(
			"NOT_ADMIN",
			HttpStatus.FORBIDDEN,
			"only admins can remove members"
		);
	}
}
