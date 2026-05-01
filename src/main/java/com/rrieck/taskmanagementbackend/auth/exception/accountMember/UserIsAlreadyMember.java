package com.rrieck.taskmanagementbackend.auth.exception.accountMember;

import com.rrieck.taskmanagementbackend.common.error.OutgoingException;
import org.springframework.http.HttpStatus;

public class UserIsAlreadyMember extends OutgoingException {
	public UserIsAlreadyMember() {
		super(
			"USER_IS_ALREADY_MEMBER",
			HttpStatus.CONFLICT,
			"the user is already member of this account"
		);
	}
}
