package com.rrieck.taskmanagementbackend.user.controller;

import com.rrieck.taskmanagementbackend.user.service.GetUserResponseByEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final GetUserResponseByEmail getUserResponseByEmail;
}
