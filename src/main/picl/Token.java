package main.picl;

import main.tokens.IToken;

import java.util.Objects;

/**
 * The {@code Token} class represents an {@code IToken} of the PICL programming language created by Niklaus Wirth.
 */
public final class Token implements IToken {

    /**
     * The {@code TokenType} enum specifies the individual syntatic elements of the PIC language.
     */
    public enum TokenType {
        AST, SLASH, PLUS, MINUS, NOT, AND, OR, EQL, NEQ, GEQ, LSS, LEQ, GTR, PERIOD, COMMA, COLON, OP, QUERY, LPAREN,
        BECOMES, IDENT, IF, WHILE, REPEAT, INC, DEC, ROL, ROR, NUMBER, RPAREN, THEN, DO, SEMICOLON, END, ELSE, ELSIF,
        UNTIL, RETURN, INT, SET, BOOL, CONST, BEGIN, PROCED, MODULE, EOF
    }

    private final TokenType type;
    private final String lexeme;
    private final Object value;

    /**
     * Creates a new {@code Token} of the specified {@code type}.
     *
     * @param type the semantic value of the returned {@code Token}
     * @throws NullPointerException if the specified {@code type} is {@code null}
     */
    public Token(TokenType type) {
        this(type, null, null);
    }

    /**
     * Creates a new {@code Token} of the specified {@code type}, associated with the specified {@code lexeme}, and
     * carrying the specified {@code value}.
     *
     * @param type the semantic value of the returned {@code Token}
     * @param lexeme the character literal that the returned {@code Token} represents
     * @param value the semantic content of the returned {@code Token}
     * @throws NullPointerException if the specified {@code type} is {@code null}
     */
    public Token(TokenType type, String lexeme, Object value) {
        this.type = Objects.requireNonNull(type);
        this.lexeme = lexeme;
        this.value = value;
    }

    @Override
    public Enum<?> getType() {
        return type;
    }

    @Override
    public boolean isEOF() {
        return type == TokenType.EOF;
    }

    @Override
    public String getLexeme() {
        return lexeme;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        Token token = (Token) obj;
        return type == token.type && (value == null || value.equals(token.value));
    }

    @Override
    public String toString() {
        return type + (value == null ? "" : " " + value);
    }

}
