package com.rrieck.taskmanagementbackend.auth.service.userDetails;

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

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findOptByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return toSpringUser(user);
	}

	public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
		User user = userRepository.findById(UserId.fromString(userId))
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return toSpringUser(user);
	}

	private UserDetails toSpringUser(User user) {
		return new org.springframework.security.core.userdetails.User(
			user.getEmail(),
			user.getPassword(),
			List.of(new SimpleGrantedAuthority("Role:" + user.getRole().name()))
		);
	}
}
