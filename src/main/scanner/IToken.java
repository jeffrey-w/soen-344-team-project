package main.scanner;

/**
 * The {@code IToken} interface specifies operations on the lexical units of a formal language. {@code IToken}s
 * constitute the smallest semantic-carrying elements of a language.
 */
public interface IToken {

    /**
     * Provides the semantic value of this {@code IToken}.
     *
     * @return the type of this {@code IToken}
     */
    Enum<?> getType();

    /**
     * Provides the {@code Position} (i.e. line and column number) in the source file from which this {@code IToken} was
     * extracted.
     *
     * @return The {@code Position} of this {@code IToken}
     */
    Position getPosition();

    /**
     * Provides the content carried by this {@code IToken}.
     *
     * @return the value associated with this {@code IToken}
     */
    Object getValue();

    /**
     * Determines whether or not this {@code IToken} represents the end of a given string of sentences.
     *
     * @return {@code true} if this {@code IToken} does signify the end of a string of sentences
     */
    boolean isEOF();

}
