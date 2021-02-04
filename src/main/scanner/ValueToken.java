package main.scanner;

import java.util.Objects;

/**
 * The {@code ValueToken} class extends the concept of a {@code Token} to include a specific value associated with the
 * token.
 */
public class ValueToken extends Token {

    private final Object value;

    /**
     * Creates a new {@code Token} with the specified {@code type} associated with the specified {@code value}.
     *
     * @param type the {@code TokenType} of the returned {@code Token}
     * @param value the value to be associated with the returned {@code Token}
     * @throws NullPointerException if either the specified {@code type} or {@code value} is {@code null}
     */
    public ValueToken(TokenType type, Object value) {
        super(type);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return super.toString() + " " + value;
    }
}
