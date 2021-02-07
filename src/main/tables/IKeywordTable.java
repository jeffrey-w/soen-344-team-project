package main.tables;

import main.tokens.IToken;

/**
 * The {@code IKeywordTable} specifies operations on collection of reserved words in a formal language.
 */
public interface IKeywordTable {

    /**
     * Provides the {@code IToken} for the specified {@code lexeme}. If the specified {@code lexeme} does not spell a
     * reserved word, a default {@code IToken} is returned.
     *
     * @param lexeme the character string literal of the reserved word being read
     * @return the {@code IToken} for the reserved word represented by the specified {@code lexeme}
     */
    IToken tokenFor(String lexeme);

}
