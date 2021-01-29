package scanner;

import java.util.Objects;

public class ValueToken extends Token {

    private final Object value;

    public ValueToken(TokenType type, Object value) {
        super(type);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return super.toString() + " " + value;
    }
}
