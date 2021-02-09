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

    private final Position currentPosition;
    private final String sourceFileContent;

    /**
     * Creates a new {@code AbstractScanner} to read the specified {@code source} file.
     *
     * @param sourceFileContent the source file content that will be read by the returned {@code AbstractScanner}
     * @throws NullPointerException if the specified {@code source} is {@code null}
     */
    protected AbstractScanner(String sourceFileContent) {
        this.sourceFileContent = Objects.requireNonNull(sourceFileContent);
        this.currentPosition = new Position();
    }

    @Override
    public IToken getToken() {
        skipWhitespace();
        currentPosition.collapse();
        System.out.println(currentPosition);
        switch (nextCharacter()) {
            case '\0':
                return new Token(Token.TokenType.EOF);
            case '*':
                return new Token(Token.TokenType.AST);
            case '/':
                return new Token(Token.TokenType.SLASH);
            case '+':
                return new Token(Token.TokenType.PLUS);
            case '-':
                return new Token(Token.TokenType.MINUS);
            case '~':
                return new Token(Token.TokenType.NOT);
            case '&':
                return new Token(Token.TokenType.AND);
            case '=':
                return new Token(Token.TokenType.EQL);
            case '#':
                return new Token(Token.TokenType.NEQ);
            case '>':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.GEQ);
                }
                return new Token(Token.TokenType.GTR);
            case '<':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.LEQ);
                }
                return new Token(Token.TokenType.LSS);
            case '.':
                return new Token(Token.TokenType.PERIOD);
            case ',':
                return new Token(Token.TokenType.COMMA);
            case ':':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.BECOMES);
                }
                return new Token(Token.TokenType.COLON);
            case '!':
                return new Token(Token.TokenType.OP);
            case '?':
                return new Token(Token.TokenType.QUERY);
            case '(':
                return new Token(Token.TokenType.LPAREN);
            case ')':
                return new Token(Token.TokenType.RPAREN);
            case ';':
                return new Token(Token.TokenType.SEMICOLON);
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

    void skipWhitespace() {
        while (isWhitespace(peekCharacter())) {
            char space = nextCharacter();
            if (space == '\n') {
                currentPosition.incrementLine();
                currentPosition.resetColumn();
            }
        }
    }

    protected char previousCharacter() {
        if (currentPosition.getCurrent() == 0) {
            return ' '; // TODO return something else
        }
        return sourceFileContent.charAt(currentPosition.getCurrent() - 1);
    }

    protected char nextCharacter() {
        if (currentPosition.getCurrent() >= sourceFileContent.length()) {
            return '\0';
        }
        currentPosition.incrementColumn();
        return sourceFileContent.charAt(currentPosition.advance());
    }

    protected char peekCharacter() {
        if (currentPosition.getCurrent() >= sourceFileContent.length()) {
            return '\0';
        }
        return sourceFileContent.charAt(currentPosition.getCurrent());
    }

    protected String currentLexeme() {
        return sourceFileContent.substring(currentPosition.getStart(), currentPosition.getCurrent());
    }

}
