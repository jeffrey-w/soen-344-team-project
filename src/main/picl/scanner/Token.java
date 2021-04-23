package main.picl.scanner;

import main.scanner.IToken;
import main.scanner.Position;

import java.util.Objects;

/**
 * The {@code Token} class represents an {@code IToken} of the PICL programming language created by Niklaus Wirth.
 */
public final class Token implements IToken {

    /**
     * The {@code TokenType} enum specifies the individual lexical elements of the PIC language.
     */
    public enum TokenType {
        /**
         * Ast token type.
         */
        AST,
        /**
         * Slash token type.
         */
        SLASH,
        /**
         * Plus token type.
         */
        PLUS,
        /**
         * Minus token type.
         */
        MINUS,
        /**
         * Not token type.
         */
        NOT,
        /**
         * And token type.
         */
        AND,
        /**
         * Or token type.
         */
        OR,
        /**
         * Eql token type.
         */
        EQL,
        /**
         * Neq token type.
         */
        NEQ,
        /**
         * Geq token type.
         */
        GEQ,
        /**
         * Lss token type.
         */
        LSS,
        /**
         * Leq token type.
         */
        LEQ,
        /**
         * Gtr token type.
         */
        GTR,
        /**
         * Period token type.
         */
        PERIOD,
        /**
         * Comma token type.
         */
        COMMA,
        /**
         * Colon token type.
         */
        COLON,
        /**
         * Op token type.
         */
        OP,
        /**
         * Query token type.
         */
        QUERY,
        /**
         * Left parenthesis token type.
         */
        LEFT_PARENTHESIS,
        /**
         * Becomes token type.
         */
        BECOMES,
        /**
         * Identifier token type.
         */
        IDENTIFIER,
        /**
         * If token type.
         */
        IF,
        /**
         * While token type.
         */
        WHILE,
        /**
         * Repeat token type.
         */
        REPEAT,
        /**
         * Inc token type.
         */
        INC,
        /**
         * Dec token type.
         */
        DEC,
        /**
         * Rol token type.
         */
        ROL,
        /**
         * Ror token type.
         */
        ROR,
        /**
         * Number token type.
         */
        NUMBER,
        /**
         * Right parenthesis token type.
         */
        RIGHT_PARENTHESIS,
        /**
         * Then token type.
         */
        THEN,
        /**
         * Do token type.
         */
        DO,
        /**
         * Semicolon token type.
         */
        SEMICOLON,
        /**
         * End token type.
         */
        END,
        /**
         * Else token type.
         */
        ELSE,
        /**
         * Elsif token type.
         */
        ELSIF,
        /**
         * Until token type.
         */
        UNTIL,
        /**
         * Return token type.
         */
        RETURN,
        /**
         * Int token type.
         */
        INT,
        /**
         * Set token type.
         */
        SET,
        /**
         * Bool token type.
         */
        BOOL,
        /**
         * Const token type.
         */
        CONST,
        /**
         * Begin token type.
         */
        BEGIN,
        /**
         * Procedure token type.
         */
        PROCEDURE,
        /**
         * Module token type.
         */
        MODULE,
        /**
         * Eof token type.
         */
        EOF,
        /**
         * Clear token type.
         */
        CLEAR;
    }

    private final TokenType type;
    private final Position position;
    private final Object value;

    /**
     * Creates a new {@code Token} of the specified {@code type}.
     *
     * @param type     the semantic value of the returned {@code Token}
     * @param position the {@code Position} in the source file from which this {@code Token} was extracted
     * @throws NullPointerException if the specified {@code type} or {@code position} is {@code null}
     */
    public Token(TokenType type, Position position) {
        this(type, position, null);
    }

    /**
     * Creates a new {@code Token} of the specified {@code type}, at the specified {@code position}, carrying the
     * specified {@code value}.
     *
     * @param type     the semantic value of the returned {@code Token}
     * @param position the {@code Position} in the source file from which this {@code Token} was extracted
     * @param value    the content of the returned {@code Token}
     * @throws NullPointerException if the specified {@code type} or {@code position} is {@code null}
     */
    public Token(TokenType type, Position position, Object value) {
        this.type = Objects.requireNonNull(type);
        this.position = Objects.requireNonNull(position.clone());
        this.value = value;
    }

    @Override
    public Enum<?> getType() {
        return type;
    }

    @Override
    public Position getPosition() {
        return position.clone();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isEOF() {
        return type == TokenType.EOF;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, position, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        Token token = (Token) obj;
        return type == token.type && position.equals(token.position) && value == null ?
                token.value == null : value.equals(token.value);
    }

    @Override
    public String toString() {
        return type + (value == null ? "" : " " + value);
    }

}
