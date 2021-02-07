package main.scanner;

import main.tokens.IToken;

import java.util.Objects;

import static java.lang.Character.isWhitespace;

/**
 * The {@code AbstractScanner} class provides a minimal implementation of the {@code IScanner} interface. An {@code
 * AbstractScanner} reads a source file character by character and provides its representation in {@code IToken}s.
 * Subclasses must define methods for reading symbols, numbers and identifiers from the source file provided to this
 * {@code AbstractScanner}.
 */
public abstract class AbstractScanner implements IScanner {

    private final Position position;
    private final String source;

    /**
     * Creates a new {@code AbstractScanner} to read the specified {@code source} file.
     *
     * @param source the source file that will be read by the returned {@code AbstractScanner}
     * @throws NullPointerException if the specified {@code source} is {@code null}
     */
    protected AbstractScanner(String source) {
        this.source = Objects.requireNonNull(source);
        this.position = new Position();
    }

    @Override
    public IToken getToken() {
        skipWhitespace();
        position.collapse();
        if (isSymbol()) {
            return scanSymbol();
        } else if (isNumber()) {
            return scanNumber();
        } else {
            return scanIdentifier();
        }
    }

    /**
     * Determines whether or not the current character in the source file provided to this {@code AbstractScanner} is a
     * reserved symbol.
     *
     * @return {@code true} if the current character is a reserved symbol
     */
    protected abstract boolean isSymbol();

    /**
     * Consumes the next characters in the source file provided to this {@code AbstractScanner} that can be interpreted
     * as a reserved symbol.
     *
     * @return an {@code IToken} corresponding to the reserved symbol represented by the consumed characters
     */
    protected abstract IToken scanSymbol();

    /**
     * Determines whether or not the current character in the source file provided to this {@code AbstractScanner} is a
     * number.
     *
     * @return {@code true} if the current character is a number
     */
    protected abstract boolean isNumber();

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
            nextCharacter();
        }
    }

    Position getPosition() {
        return position;
    }

    char nextCharacter() {
        if (position.getCurrent() >= source.length()) {
            return '\0';
        }
        return source.charAt(position.advance());
    }

    char peekCharacter() {
        if (position.getCurrent() >= source.length()) {
            return '\0';
        }
        return source.charAt(position.getCurrent());
    }

    String currentLexeme() {
        return source.substring(position.getStart(), position.getCurrent());
    }

}
