package com.rrieck.taskmanagementbackend.auth.controller;

import com.rrieck.taskmanagementbackend.auth.dto.request.LoginUserRequest;
import com.rrieck.taskmanagementbackend.auth.dto.request.LogoutRequest;
import com.rrieck.taskmanagementbackend.auth.dto.request.RefreshAuthenticationRequest;
import com.rrieck.taskmanagementbackend.auth.dto.request.RegisterUserRequest;
import com.rrieck.taskmanagementbackend.auth.dto.response.AuthResponse;
import com.rrieck.taskmanagementbackend.auth.service.LoginUserService;
import com.rrieck.taskmanagementbackend.auth.service.LogoutUserService;
import com.rrieck.taskmanagementbackend.auth.service.RefreshAuthenticationService;
import com.rrieck.taskmanagementbackend.auth.service.RegisterUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final LoginUserService loginUserService;
	private final LogoutUserService logoutUserService;
	private final RegisterUserService registerUserService;
	private final RefreshAuthenticationService refreshAuthenticationService;

	@PostMapping("/register")
	public AuthResponse register(@Valid @RequestBody RegisterUserRequest request) {
		return registerUserService.register(
			request.name(),
			request.email(),
			request.password()
		);
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginUserRequest request) {
		return loginUserService.login(request);
	}

	@PostMapping("/refresh")
	public AuthResponse refresh(@Valid @RequestBody RefreshAuthenticationRequest request) {
		return refreshAuthenticationService.refresh(request.refreshToken());
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PostMapping("/logout")
	public void login(@Valid @RequestBody LogoutRequest request) {
		logoutUserService.logout(request.refreshToken());
	}
}
