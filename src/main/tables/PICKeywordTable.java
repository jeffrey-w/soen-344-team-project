package main.tables;

import main.tokens.IToken;
import main.tokens.PICToken;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code PICKeywordTable} class defines the reserved words for the PIC programming language, and specifies
 * operations for lexing them.
 */
public class PICKeywordTable implements IKeywordTable {

    private static final Map<String, IToken> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("BEGIN", new PICToken(PICToken.TokenType.BEGIN));
        KEYWORDS.put("END", new PICToken(PICToken.TokenType.END));
        KEYWORDS.put("INT", new PICToken(PICToken.TokenType.INT));
        KEYWORDS.put("SET", new PICToken(PICToken.TokenType.SET));
        KEYWORDS.put("BOOL", new PICToken(PICToken.TokenType.BOOL));
        KEYWORDS.put("OR", new PICToken(PICToken.TokenType.OR));
        KEYWORDS.put("INC", new PICToken(PICToken.TokenType.INC));
        KEYWORDS.put("DEC", new PICToken(PICToken.TokenType.DEC));
        KEYWORDS.put("ROL", new PICToken(PICToken.TokenType.ROL));
        KEYWORDS.put("ROR", new PICToken(PICToken.TokenType.ROR));
        KEYWORDS.put("IF", new PICToken(PICToken.TokenType.IF));
        KEYWORDS.put("THEN", new PICToken(PICToken.TokenType.THEN));
        KEYWORDS.put("ELSE", new PICToken(PICToken.TokenType.ELSE));
        KEYWORDS.put("ELSIF", new PICToken(PICToken.TokenType.ELSIF));
        KEYWORDS.put("WHILE", new PICToken(PICToken.TokenType.WHILE));
        KEYWORDS.put("DO", new PICToken(PICToken.TokenType.DO));
        KEYWORDS.put("REPEAT", new PICToken(PICToken.TokenType.REPEAT));
        KEYWORDS.put("UNTIL", new PICToken(PICToken.TokenType.UNTIL));
        KEYWORDS.put("CONST", new PICToken(PICToken.TokenType.CONST));
        KEYWORDS.put("PROCEDURE", new PICToken(PICToken.TokenType.PROCED));
        KEYWORDS.put("RETURN", new PICToken(PICToken.TokenType.RETURN));
        KEYWORDS.put("MODULE", new PICToken(PICToken.TokenType.MODULE));
    }

    @Override
    public IToken tokenFor(final String lexeme) {
        return KEYWORDS.getOrDefault(lexeme, new PICToken(PICToken.TokenType.IDENT, lexeme));
    }

}
