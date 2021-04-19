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
        AST, SLASH, PLUS, MINUS, NOT, AND, OR, EQL, NEQ, GEQ, LSS, LEQ, GTR, PERIOD, COMMA, COLON, OP, QUERY,
        LEFT_PARENTHESIS, BECOMES, IDENTIFIER, IF, WHILE, REPEAT, INC, DEC, ROL, ROR, NUMBER, RIGHT_PARENTHESIS, THEN,
        DO, SEMICOLON, END, ELSE, ELSIF, UNTIL, RETURN, INT, SET, BOOL, CONST, BEGIN, PROCEDURE, MODULE, EOF, CLEAR;
    }

    private final TokenType type;
    private final Position position;
    private final Object value;

    /**
     * Creates a new {@code Token} of the specified {@code type}.
     *
     * @param type the semantic value of the returned {@code Token}
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
     * @param type the semantic value of the returned {@code Token}
     * @param position the {@code Position} in the source file from which this {@code Token} was extracted
     * @param value the content of the returned {@code Token}
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
