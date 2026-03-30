package com.rrieck.taskmanagementbackend.auth.service.userDetails;

import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMember;
import com.rrieck.taskmanagementbackend.accountMemeber.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;
	private final AccountMemberRepository accountMemberRepository;

	@Override
	public UserDetails loadUserByUsername(
		String email
	) throws UsernameNotFoundException {
		User user = userRepository
			.findOptByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		AccountMember accountMember = accountMemberRepository.getByAccountIdAndUserId(
			user.getLastUsedAccountId(),
			user.getId()
		);

		return new org.springframework.security.core.userdetails.User(
			user.getId().id().toString(),
			user.getPassword(),
			List.of(new SimpleGrantedAuthority("Role_" + accountMember.getRole().name()))
		);
	}
}
