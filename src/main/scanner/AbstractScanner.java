package main.scanner;

import main.picl.Token;
import main.tokens.IToken;

import java.util.Objects;

import static java.lang.Character.isWhitespace;

/**
 * The {@code AbstractScanner} class provides a minimal implementation of the {@code IScanner} interface. An {@code
 * AbstractScanner} reads a source file character by character and provides its representation in {@code IToken}s.
 * Subclasses must define methods for reading numbers and identifiers.
 */
public abstract class AbstractScanner implements IScanner {

    private int startIndex, currentIndex;
    private final Position position;
    private final String sourceFileContent;

    /**
     * Creates a new {@code AbstractScanner} to read the specified {@code source} file.
     *
     * @param sourceFileContent the source file content that will be read by the returned {@code AbstractScanner}
     * @throws NullPointerException if the specified {@code source} is {@code null}
     */
    protected AbstractScanner(String sourceFileContent) {
        this.sourceFileContent = Objects.requireNonNull(sourceFileContent);
        this.position = new Position();
    }

    @Override
    public IToken getToken() {
        skipWhitespace();
        startIndex = currentIndex;
        switch (nextCharacter()) {
            case '\0':
                return new Token(Token.TokenType.EOF, currentPosition());
            case '*':
                return new Token(Token.TokenType.AST, currentPosition());
            case '/':
                return new Token(Token.TokenType.SLASH, currentPosition());
            case '+':
                return new Token(Token.TokenType.PLUS, currentPosition());
            case '-':
                return new Token(Token.TokenType.MINUS, currentPosition());
            case '~':
                return new Token(Token.TokenType.NOT, currentPosition());
            case '&':
                return new Token(Token.TokenType.AND, currentPosition());
            case '=':
                return new Token(Token.TokenType.EQL, currentPosition());
            case '#':
                return new Token(Token.TokenType.NEQ, currentPosition());
            case '>':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.GEQ, currentPosition());
                }
                return new Token(Token.TokenType.GTR, currentPosition());
            case '<':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.LEQ, currentPosition());
                }
                return new Token(Token.TokenType.LSS, currentPosition());
            case '.':
                return new Token(Token.TokenType.PERIOD, currentPosition());
            case ',':
                return new Token(Token.TokenType.COMMA, currentPosition());
            case ':':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.BECOMES, currentPosition());
                }
                return new Token(Token.TokenType.COLON, currentPosition());
            case '!':
                return new Token(Token.TokenType.OP, currentPosition());
            case '?':
                return new Token(Token.TokenType.QUERY, currentPosition());
            case '(':
                return new Token(Token.TokenType.LEFT_PARENTHESIS, currentPosition());
            case ')':
                return new Token(Token.TokenType.RIGHT_PARENTHESIS, currentPosition());
            case ';':
                return new Token(Token.TokenType.SEMICOLON, currentPosition());
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
     * @return a number {@code IToken}
     */
    protected abstract IToken scanNumber();

    /**
     * Consumes the next characters in the source file provided to this {@code AbstractScanner} that can be interpreted
     * as an identifier.
     *
     * @return an identifier {@code IToken}
     */
    protected abstract IToken scanIdentifier();

    /**
     * Consumes the next characters in the source file provided to this {@code AbstractScanner} that can be interpreted
     * as whitespace.
     */
    protected void skipWhitespace() {
        while (isWhitespace(peekCharacter())) {
            if (nextCharacter() == '\n') {
                position.incrementLine();
            }
        }
    }

    /**
     * Provides the character most recently read by this {@code AbstractScanner}.
     *
     * @return the character that this {@code AbstractScanner} last scanned
     * @throws IllegalStateException if this {@code AbstractScanner} has not read any characters yet
     */
    protected char previousCharacter() {
        if (currentIndex == 0) {
            throw new IllegalStateException("This scanner has no previous character.");
        }
        return sourceFileContent.charAt(currentIndex - 1);
    }

    /**
     * Provides the next character in the source file provided to this {@code AbstractScanner}, and advances its
     * cursor.
     *
     * @return the next character in the source file provided to this {@code AbstractScanner}
     */
    protected char nextCharacter() {
        if (currentIndex >= sourceFileContent.length()) {
            return '\0';
        }
        position.incrementColumn();
        return sourceFileContent.charAt(currentIndex++);
    }

    /**
     * Provides the next character in the source file provided to this {@code AbstractScanner}, but does not advance its
     * cursor.
     *
     * @return the next character in the source file provided to this {@code AbstractScanner}
     */
    protected char peekCharacter() {
        if (currentIndex >= sourceFileContent.length()) {
            return '\0';
        }
        return sourceFileContent.charAt(currentIndex);
    }

    /**
     * Provides the character string currently selected by this {@code AbstractScanner}.
     *
     * @return the word most recently read by this {@code AbstractScanner}
     */
    protected String currentLexeme() {
        return sourceFileContent.substring(startIndex, currentIndex);
    }

    /**
     * Provides the line and column number that this {@code AbstractScanner} is at in the source file previously
     * provided to it.
     *
     * @return the current {@code Position} of this {@code AbstractScanner}
     */
    protected Position currentPosition() {
        return position.clone();
    }

}
