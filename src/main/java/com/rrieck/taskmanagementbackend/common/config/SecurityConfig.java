package com.rrieck.taskmanagementbackend.common.config;

import com.rrieck.taskmanagementbackend.auth.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CorsProperties corsProperties;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		return http.csrf(AbstractHttpConfigurer::disable)
		           .cors(Customizer.withDefaults())
		           .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		           .authorizeHttpRequests(auth ->
			           auth.requestMatchers("/api/auth/**").permitAll()
			               .anyRequest().authenticated()
		           )
		           .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		           .build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(corsProperties.getAllowedOriginPatterns());
		configuration.setAllowedMethods(corsProperties.getAllowedMethods());
		configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
		configuration.setAllowCredentials(corsProperties.isAllowCredentials());

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
