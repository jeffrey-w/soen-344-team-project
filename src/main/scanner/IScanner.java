package main.scanner;

/**
 * The {@code IScanner} interface specifies the operations for reading {@code Token}s from a source file.
 */
public interface IScanner {

    /**
     * Provides the next {@code Token} in the source file provided to this {@code IScanner}.
     *
     * @return the next {@code Token} in the source file provided to this {@code IScanner}
     */
    Token getToken();

}
