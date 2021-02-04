package main.scanner;

import java.util.Objects;

import static java.lang.Character.isWhitespace;

/**
 * The {@code AbstractScanner} class provides a minimal implementation of the {@code IScanner} interface. Subclasses
 * must define methods for reading numbers and identifiers from a source file.
 */
public abstract class AbstractScanner implements IScanner {

    private int start, current;
    private final String source;

    /**
     * Creates a new {@code AbstractScanner} to read the specified {@code source} file.
     *
     * @param source the source file that will be read by the returned {@code AbstractScanner}
     * @throws NullPointerException if the specified {@code source} is {@code null}
     */
    protected AbstractScanner(String source) {
        this.source = Objects.requireNonNull(source);
    }

    @Override
    public Token getToken() {
        skipWhitespace();
        switch (nextCharacter()) {
            case '\0':
                return Token.EOF;
            case '*':
                return Token.AST;
            case '/':
                return Token.SLASH;
            case '+':
                return Token.PLUS;
            case '-':
                return Token.MINUS;
            case '~':
                return Token.NOT;
            case '&':
                return Token.AND;
            case '=':
                return Token.EQL;
            case '#':
                return Token.NEQ;
            case '>':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return Token.GEQ;
                }
                return Token.GTR;
            case '<':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return Token.LEQ;
                }
                return Token.LSS;
            case '.':
                return Token.PERIOD;
            case ',':
                return Token.COMMA;
            case ':':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return Token.BECOMES;
                }
                return Token.COLON;
            case '!':
                return Token.OP;
            case '?':
                return Token.QUERY;
            case '(':
                return Token.LPAREN;
            case ')':
                return Token.RPAREN;
            case ';':
                return Token.SEMICOLON;
            case '$':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return scanNumber();
            default:
                return scanIdentifier();
        }
    }

    /**
     * Consumes the next characters in the source file provided to this {@code AbstractScanner} that can be interpreted
     * as a number.
     *
     * @return a {@link Token#NUMBER number} {@code Token}
     */
    protected abstract Token scanNumber();

    /**
     * Consumes the next characters in the source file provided to this {@code AbstractScanner} that can be interpreted
     * as an identifier.
     *
     * @return the {@code Token} that corresponds to the reserved word scanned, or an {@link Token#IDENT identifier}
     * {@code Token}
     */
    protected abstract Token scanIdentifier();

    void skipWhitespace() {
        while (isWhitespace(peekCharacter())) {
            nextCharacter();
        }
        start = current;
    }

    char previousCharacter() {
        return source.charAt(current - 1);
    }

    char nextCharacter() {
        if (current >= source.length()) {
            return '\0';
        }
        return source.charAt(current++);
    }

    char peekCharacter() {
        if (current >= source.length()) {
            return '\0';
        }
        return source.charAt(current);
    }

    void consume() {
        start++;
    }

    String currentLexeme() {
        return source.substring(start, current);
    }

}
