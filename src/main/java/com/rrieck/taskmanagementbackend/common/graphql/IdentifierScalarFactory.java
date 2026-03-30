package com.rrieck.taskmanagementbackend.common.graphql;

import com.rrieck.taskmanagementbackend.common.identifier.Identifier;
import graphql.language.StringValue;
import graphql.schema.*;

import java.util.function.Function;

public class IdentifierScalarFactory {
	private IdentifierScalarFactory() {
	}

	public static <T extends Identifier> GraphQLScalarType identifierScalar(
		String name,
		Function<String, T> parser
	) {
		return GraphQLScalarType
			.newScalar()
			.name(name)
			.description("Identifier scalar for " + name)
			.coercing(new Coercing<T, String>() {
				@Override
				public String serialize(Object input) {
					if (!(input instanceof Identifier identifier)) {
						throw new CoercingSerializeException("Expected Identifier");
					}
					return identifier.id().toString();
				}

				@Override
				public T parseValue(Object input) {
					if (!(input instanceof String value)) {
						throw new CoercingParseValueException("Expected String");
					}

					try {
						return parser.apply(value);
					} catch (RuntimeException exception) {
						throw new CoercingParseValueException("Invalid identifier", exception);
					}
				}

				@Override
				public T parseLiteral(Object input) {
					if (!(input instanceof StringValue value)) {
						throw new CoercingParseLiteralException("Expected String literal");
					}

					try {
						return parser.apply(value.getValue());
					} catch (RuntimeException exception) {
						throw new CoercingParseLiteralException("Invalid identifier", exception);
					}
				}
			})
			.build();
	}
}
