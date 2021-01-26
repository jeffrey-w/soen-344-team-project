import java.util.Objects;

public class Token {

    public enum TokenType {
        NULL,
        AST,
        SLASH,
        PLUS,
        MINUS,
        NOT,
        AND,
        OR,
        EQL,
        NEQ,
        GEQ,
        LSS,
        LEQ,
        GTR,
        PERIOD,
        COMMA,
        COLON,
        OP,
        QUERY,
        LPAREN,
        BECOMES,
        IDENT,
        IF,
        WHILE,
        REPEAT,
        INC,
        DEC,
        ROL,
        ROR,
        NUMBER,
        RPAREN,
        THEN,
        DO,
        SEMICOLON,
        END,
        ELSE,
        ELSIF,
        UNTIL,
        RETURN,
        INT,
        SET,
        BOOL,
        CONST,
        BEGIN,
        PROCED,
        MODULE,
        EOF
    }

    private final TokenType type;

    Token(TokenType type) {
        this.type = Objects.requireNonNull(type);
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
