package com.rrieck.taskmanagementbackend.auth.service.invitation;

import com.rrieck.taskmanagementbackend.auth.exception.account.AccountNotFound;
import com.rrieck.taskmanagementbackend.auth.exception.user.UserNotFound;
import com.rrieck.taskmanagementbackend.auth.model.account.Account;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMember;
import com.rrieck.taskmanagementbackend.auth.model.invitation.Invitation;
import com.rrieck.taskmanagementbackend.auth.model.invitation.InvitationId;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.AccountRepository;
import com.rrieck.taskmanagementbackend.auth.repository.InvitationRepository;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.schema.accountMembers.AccountMemberTypes;
import com.rrieck.taskmanagementbackend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InviteToAccountService {
	private final AccountMemberRepository accountMemberRepository;
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;
	private final InvitationRepository invitationRepository;
	private final EmailService emailService;


	public AccountMemberTypes.AccountMemberWithInvitationType invite(AccountId accountId, UserId invitedByUser, List<String> inviteeEmails) {
		try {
			UUID invitationCode = UUID.randomUUID();

			Account account = accountRepository.getOptById(accountId).orElseThrow(() -> new AccountNotFound(accountId));
			User user = userRepository.getOptById(invitedByUser).orElseThrow(() -> new UserNotFound(invitedByUser));
			List<Invitation> existingInvitations = invitationRepository.getByInvitedByAccount(accountId);

			List<AccountMember> existingMembers = accountMemberRepository
				.getAllByAccountId(accountId);

			List<String> existingEmails = existingMembers
				.stream()
				.map(AccountMember::getUser)
				.map(User::getEmail)
				.toList();

			List<Invitation> newInvitations = inviteeEmails
				.stream()
				.map(String::trim)
				.map(String::toLowerCase)
				.filter(email -> !existingEmails.contains(email))
				.map(email -> Invitation
					.builder()
					.id(InvitationId.generateId())
					.invitationCode(invitationCode)
					.inviteeEmail(email)
					.invitedByUser(invitedByUser)
					.invitedByAccount(accountId)
					.expirationDate(LocalDateTime.now().plusDays(3))
					.build()
				)
				.toList();

			String url = "http://localhost:5173/accept-invite/" + invitationCode;

			//ToDo bring into message queue
			newInvitations.forEach(invitation ->
				emailService.sendInvitationEmail(
					invitation.getInviteeEmail(),
					account.getName(),
					user.getName(),
					url
				)
			);

			invitationRepository.saveAll(newInvitations);

			List<Invitation> allInvitations = Stream
				.concat(
					existingInvitations.stream(),
					newInvitations.stream()
				)
				.toList();

			return AccountMemberTypes.AccountMemberWithInvitationType.from(account.getName(), existingMembers, allInvitations);
		} catch (Exception e) {
			LoggerFactory.getLogger(InviteToAccountService.class).error("Error while inviting users", e);
			throw e;
		}
	}
}
