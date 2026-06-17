package com.rrieck.taskmanagementbackend.common.graphql;

import graphql.language.StringValue;
import graphql.schema.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeScalarFactory {
	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

	private DateTimeScalarFactory() {
	}

	public static GraphQLScalarType dateTimeScalar() {
		return GraphQLScalarType
			.newScalar()
			.name("DateTime")
			.description("ISO-8601 DateTime scalar")
			.coercing(new Coercing<LocalDateTime, String>() {
				@Override
				public String serialize(Object input) {
					if (!(input instanceof LocalDateTime dateTime)) {
						throw new CoercingSerializeException("Expected LocalDateTime, got " + input.getClass()
						                                                                           .getName());
					}
					return dateTime.format(ISO_FORMATTER);
				}

				@Override
				public LocalDateTime parseValue(Object input) {
					if (!(input instanceof String value)) {
						throw new CoercingParseValueException("Expected String");
					}

					try {
						return LocalDateTime.parse(value, ISO_FORMATTER);
					} catch (Exception exception) {
						throw new CoercingParseValueException("Invalid DateTime format", exception);
					}
				}

				@Override
				public LocalDateTime parseLiteral(Object input) {
					if (!(input instanceof StringValue value)) {
						throw new CoercingParseLiteralException("Expected String literal");
					}

					try {
						return LocalDateTime.parse(value.getValue(), ISO_FORMATTER);
					} catch (Exception exception) {
						throw new CoercingParseLiteralException("Invalid DateTime format", exception);
					}
				}
			})
			.build();
	}
}