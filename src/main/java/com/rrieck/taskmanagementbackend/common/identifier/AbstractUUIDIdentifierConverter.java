package com.rrieck.taskmanagementbackend.common.identifier;

import jakarta.persistence.AttributeConverter;

import java.util.UUID;
import java.util.function.Function;


public abstract class AbstractUUIDIdentifierConverter<T extends Identifier>
	implements AttributeConverter<T, UUID> {

	private final Function<T, UUID> toUuid;
	private final Function<UUID, T> fromUuid;

	protected AbstractUUIDIdentifierConverter(Function<T, UUID> toUuid,
		Function<UUID, T> fromUuid) {
		this.toUuid = toUuid;
		this.fromUuid = fromUuid;
	}

	@Override
	public UUID convertToDatabaseColumn(T attribute) {
		return attribute == null ? null : toUuid.apply(attribute);
	}

	@Override
	public T convertToEntityAttribute(UUID dbData) {
		return dbData == null ? null : fromUuid.apply(dbData);
	}
}