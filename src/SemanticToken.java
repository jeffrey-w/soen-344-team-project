import java.util.Objects;

public class SemanticToken extends Token {

    private final Object value;

    public SemanticToken(TokenType type, Object value) {
        super(type);
        this.value = Objects.requireNonNull(value);
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString() + " " + value;
    }
}
