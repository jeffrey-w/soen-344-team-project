package main.tokens;

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
     * Determines whether or not this {@code IToken} represents the end of a given string of sentences.
     *
     * @return {@code true} if this {@code IToken} does signify the end of a string of sentences
     */
    boolean isEOF();

    /**
     * Provides the character string literal that this {@code IToken} represents.
     *
     * @return the word this {@code IToken} categorizes
     */
    String getLexeme();

    /**
     * Provides the semantic content of this {@code IToken}.
     *
     * @return the value associated with this {@code IToken}. // TODO rewrite this
     */
    Object getValue();

}
