package com.rrieck.taskmanagementbackend.email.service;

public class SanitizeEmail {
	public static String sanitize(String email) {
		return email.toLowerCase().trim();
	}
}
