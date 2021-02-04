package main.scanner;

import java.util.Objects;

/**
 * The {@code Token} class represents a lexical unit or a source file. It represents the smallest chunk of meaning in a
 * string that belongs to a formal language.
 */
public class Token {

    /**
     * The kinds of {@code Token}s, the combination of which, constitute valid PIC language strings.
     */
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

    /**
     * Creates a new {@code Token} of the specified {@code type}.
     *
     * @param type the {@code TokenType} of the returned {@code Token}
     * @throws NullPointerException if the specified {@code type} is {@code null}
     */
    Token(TokenType type) {
        this.type = Objects.requireNonNull(type);
    }

    /**
     * Provides the type of this {@code Token}.
     *
     * @return the {@code TokenType} of this {@code Token}
     */
    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }

}
