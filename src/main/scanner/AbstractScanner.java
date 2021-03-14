package main.scanner;

import java.util.Objects;

import static java.lang.Character.isWhitespace;

/**
 * The {@code AbstractScanner} class provides a minimal implementation of the {@code IScanner} interface. An {@code
 * AbstractScanner}* reads a source file character by character and provides its representation in {@code IToken}s.
 * Subclasses must define methods for reading operators, numbers, and identifiers.
 */
public abstract class AbstractScanner implements IScanner {

    private int startIndex, currentIndex;
    private final Position position;
    private final String sourceFileContent;

    /**
     * Creates a new {@code AbstractScanner} to read the specified {@code sourceFileContent}.
     *
     * @param sourceFileContent the source file that will be read by the returned {@code AbstractScanner}
     * @throws NullPointerException if the specified {@code sourceFileContent} is {@code null}
     */
    protected AbstractScanner(String sourceFileContent) {
        this.position = new Position();
        this.sourceFileContent = Objects.requireNonNull(sourceFileContent);
    }

    @Override
    public final IToken getToken() {
        skipWhitespace();
        startIndex = currentIndex;
        if (isOperator()) {
            return scanOperator();
        } else if (isNumeric()) {
            return scanNumber();
        } else {
            return scanIdentifier();
        }
    }

    /**
     * Determines whether or not the current character in the source file provided to this {@code AbstractScanner} is an
     * operator.
     *
     * @return {@code true} if the current character is an operator
     */
    protected abstract boolean isOperator();

    /**
     * Consumes the next characters in the source file provided to this {@code AbstractScanner} that can be interpreted
     * as an operator.
     *
     * @return an {@code IToken} corresponding to the operator represented by the consumed characters
     * @throws java.util.NoSuchElementException if this method is called when the next characters to be consumed do not represent an operator; this can be prevented by calling {@link #isOperator()}
     */
    protected abstract IToken scanOperator();

    /**
     * Determines whether or not the current character in the source file provided to this {@code AbstractScanner} is a
     * number.
     *
     * @return {@code true} if the current character is a number
     */
    protected abstract boolean isNumeric();

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
