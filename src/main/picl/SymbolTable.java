package main.picl;

import main.scanner.ISymbolTable;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code SymbolTable} class provides an interface to the reserved words for the PICL programming language created
 * by Niklaus Wirth.
 */
public final class SymbolTable implements ISymbolTable {

    private static final Map<String, Enum<?>> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("BEGIN", Token.TokenType.BEGIN);
        KEYWORDS.put("END", Token.TokenType.END);
        KEYWORDS.put("INT", Token.TokenType.INT);
        KEYWORDS.put("SET", Token.TokenType.SET);
        KEYWORDS.put("BOOL", Token.TokenType.BOOL);
        KEYWORDS.put("OR", Token.TokenType.OR);
        KEYWORDS.put("INC", Token.TokenType.INC);
        KEYWORDS.put("DEC", Token.TokenType.DEC);
        KEYWORDS.put("ROL", Token.TokenType.ROL);
        KEYWORDS.put("ROR", Token.TokenType.ROR);
        KEYWORDS.put("IF", Token.TokenType.IF);
        KEYWORDS.put("THEN", Token.TokenType.THEN);
        KEYWORDS.put("ELSE", Token.TokenType.ELSE);
        KEYWORDS.put("ELSIF", Token.TokenType.ELSIF);
        KEYWORDS.put("WHILE", Token.TokenType.WHILE);
        KEYWORDS.put("DO", Token.TokenType.DO);
        KEYWORDS.put("REPEAT", Token.TokenType.REPEAT);
        KEYWORDS.put("UNTIL", Token.TokenType.UNTIL);
        KEYWORDS.put("CONST", Token.TokenType.CONST);
        KEYWORDS.put("PROCEDURE", Token.TokenType.PROCEDURE);
        KEYWORDS.put("RETURN", Token.TokenType.RETURN);
        KEYWORDS.put("MODULE", Token.TokenType.MODULE);
    }

    @Override
    public void addTypeFor(String lexeme, Enum<?> type) {
        KEYWORDS.putIfAbsent(lexeme, type);
    }

    @Override
    public Enum<?> getTypeFor(String lexeme) {
        return KEYWORDS.getOrDefault(lexeme, Token.TokenType.IDENTIFIER);
    }

}
