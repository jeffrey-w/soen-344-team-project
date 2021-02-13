package main.scanner;

/**
 * The {@code ISymbolTable} interface specifies operations on a collection of reserved words in a formal grammar.
 * Reserved words are defined as key value pairs between their character string literal (or lexeme) and their {@code
 * IToken} type.
 */
public interface ISymbolTable {

    /**
     * Inserts a new reserved word into this {@code ISymbolTable}, specified by the given {@code lexeme} and {@code
     * type}
     *
     * @param lexeme the character string literal of the newly inserted reserved word
     * @param type the {@code IToken} type of the newly inserted reserved word
     */
    void addTypeFor(String lexeme, Enum<?> type);

    /**
     * Provides the {@code IToken} type for the reserved word in this {@code ISymbolTable} specified by the given {@code
     * lexeme}. Should a reserved word with the specified {@code lexeme} not exist in this {@code ISymbolTable}, a
     * default type is returned.
     *
     * @param lexeme a character string literal for a reserved word in this {@code ISymbolTable}
     * @return the {@code IToken} type for the reserved word specified by the given {@code lexeme}
     */
    Enum<?> getTypeFor(String lexeme);

}
