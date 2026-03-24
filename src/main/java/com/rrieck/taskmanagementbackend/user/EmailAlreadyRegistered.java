package com.rrieck.taskmanagementbackend.user;

public class EmailAlreadyRegistered extends RuntimeException {
	public EmailAlreadyRegistered(String message) {
		super(message);
	}
}
