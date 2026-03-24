package com.rrieck.taskmanagementbackend.user.controller;

import com.rrieck.taskmanagementbackend.user.dto.EditUserRequest;
import com.rrieck.taskmanagementbackend.user.dto.UserResponse;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import com.rrieck.taskmanagementbackend.user.service.EditUserService;
import com.rrieck.taskmanagementbackend.user.service.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final GetUserByIdService getUserByIdService;
	private final EditUserService editUserService;

	@GetMapping("/{userId}")
	public UserResponse getUser(@PathVariable String userId) {
		return getUserByIdService.get(UserId.fromString(userId));
	}

	@PostMapping("/{userId}")
	public UserResponse getUser(@PathVariable String userId, @RequestBody EditUserRequest request) {
		return editUserService.edit(
			UserId.fromString(userId),
			request.nameOpt(),
			request.emailOpt(),
			request.passwordOpt()
		);
	}
}
