package com.rrieck.taskmanagementbackend.common.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;

	public void sendInvitationEmail(String to, String accountName, String inviterName, String inviteURL) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Invitation to join " + accountName);
		message.setText(
			"Hello,\n\n" +
				inviterName + " has invited you to join " + accountName + ".\n\n" +
				"Accept the invitation to get started.\n\n" +
				"Best regards,\nTask Management Team"
		);
		mailSender.send(message);
	}

	public void sendWelcomeEmail(String to, String name) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Welcome to Task Management");
		message.setText("Hello " + name + ",\n\nWelcome to our platform!");
		mailSender.send(message);
	}
}