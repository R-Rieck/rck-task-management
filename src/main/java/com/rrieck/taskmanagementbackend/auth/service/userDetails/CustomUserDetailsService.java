package com.rrieck.taskmanagementbackend.auth.service.userDetails;

import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMember;
import com.rrieck.taskmanagementbackend.accountMemeber.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
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
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findOptByEmail(email)
		                          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		AccountMember accountMember = accountMemberRepository.getByAccountIdAndUserId(user.getLastUsedAccountId(), user.getId());

		return toSpringUser(user, accountMember);
	}

	public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
		User user = userRepository.findById(UserId.fromString(userId))
		                          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		AccountMember accountMember = accountMemberRepository.getByAccountIdAndUserId(user.getLastUsedAccountId(), user.getId());
		return toSpringUser(user, accountMember);
	}

	private UserDetails toSpringUser(User user, AccountMember accountMember) {
		return new org.springframework.security.core.userdetails.User(
			user.getEmail(),
			user.getPassword(),
			List.of(new SimpleGrantedAuthority("Role:" + accountMember.getRole().name()))
		);
	}
}
