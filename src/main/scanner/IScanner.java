package main.scanner;

/**
 * The {@code IScanner} interface specifies operations for reading {@code IToken}s from a source file.
 */
public interface IScanner {

    /**
     * Consumes the next characters in the source file provided to this {@code IScanner} that can be interpreted as an
     * {@code IToken} and returns that token.
     *
     * @return the next {@code IToken} in the source file provided to this {@code IScanner}
     */
    IToken getToken();

}
