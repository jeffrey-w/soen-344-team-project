package main.tokens;

import java.util.Objects;

/**
 * The {@code PICToken} class represents an {@code IToken} of the PIC programming language created by Niklaus Wirth.
 */
public final class PICToken implements IToken {

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

    /**
     * Creates a new {@code PICToken} of the specified {@code type}.
     *
     * @param type the semantic value of the returned {@code PICToken}
     * @throws NullPointerException if the specified {@code type} is {@code null}
     */
    public PICToken(TokenType type) {
        this(type, null);
    }

    /**
     * Creates a new {@code PICToken} of the specified {@code type} associated with the specified {@code lexeme}.
     *
     * @param type the semantic value of the returned {@code PICToken}
     * @param lexeme the character literal that the returned {@code PICToken} represents
     * @throws NullPointerException if the specified {@code type} is {@code null}
     */
    public PICToken(TokenType type, String lexeme) {
        this.type = Objects.requireNonNull(type);
        this.lexeme = lexeme;
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
    public int hashCode() {
        return Objects.hash(type, lexeme);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PICToken)) {
            return false;
        }
        PICToken token = (PICToken) obj;
        return type == token.type && (lexeme == null || lexeme.equals(token.lexeme));
    }

    @Override
    public String toString() {
        return type + (lexeme == null ? "" : " " + lexeme);
    }

}
