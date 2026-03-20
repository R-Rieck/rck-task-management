package com.rrieck.taskmanagementbackend.user.repository;

import com.rrieck.taskmanagementbackend.common.identifier.AbstractUUIDIdentifierConverter;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import jakarta.persistence.Converter;

@Converter
public class UserIdConverter extends AbstractUUIDIdentifierConverter<UserId> {
	public UserIdConverter() {
		super(UserId::id, UserId::new);
	}
}
