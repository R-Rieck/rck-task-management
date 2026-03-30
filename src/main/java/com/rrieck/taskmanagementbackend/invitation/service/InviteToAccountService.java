package com.rrieck.taskmanagementbackend.invitation.service;

import com.rrieck.taskmanagementbackend.account.model.Account;
import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.account.repository.AccountRepository;
import com.rrieck.taskmanagementbackend.accountMember.model.AccountMember;
import com.rrieck.taskmanagementbackend.accountMember.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.common.email.EmailService;
import com.rrieck.taskmanagementbackend.invitation.model.Invitation;
import com.rrieck.taskmanagementbackend.invitation.model.InvitationId;
import com.rrieck.taskmanagementbackend.invitation.repository.InvitationRepository;
import com.rrieck.taskmanagementbackend.invitation.schema.InvitationTypes;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteToAccountService {
	private final AccountMemberRepository accountMemberRepository;
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;
	private final InvitationRepository invitationRepository;
	private final EmailService emailService;


	public InvitationTypes.InvitationType invite(AccountId accountId, UserId inviter, List<String> inviteeEmails) {
		try {
			UUID invitationCode = UUID.randomUUID();

			Account account = accountRepository.getReferenceById(accountId);
			User user = userRepository.getReferenceById(inviter);
			List<String> existingMemberMails = accountMemberRepository
				.getAllByAccountId(accountId)
				.stream()
				.map(AccountMember::getUser)
				.map(User::getEmail)
				.toList();

			List<Invitation> newInvitations = inviteeEmails
				.stream()
				.map(String::trim)
				.map(String::toLowerCase)
				.filter(email -> !existingMemberMails.contains(email))
				.map(email -> Invitation
					.builder()
					.id(InvitationId.generateId())
					.invitationCode(invitationCode)
					.invitedEmail(email)
					.invitedBy(inviter)
					.expirationDate(LocalDateTime.now().plusDays(3))
					.build()
				)
				.toList();

			String url = "http://localhost:5173/accept-invite/" + invitationCode;

			newInvitations.forEach(invitation ->
				emailService.sendInvitationEmail(invitation.getInvitedEmail(), account.getName(), user.getName(), url)
			);

			invitationRepository.saveAll(newInvitations);

			return InvitationTypes.InvitationType
				.builder()
				.invitesSent(newInvitations.size())
				.build();
		} catch (Exception e) {
			LoggerFactory.getLogger(InviteToAccountService.class).error("Error while inviting users", e);
			return InvitationTypes.InvitationType
				.builder()
				.invitesSent(0)
				.build();
		}
	}
}
