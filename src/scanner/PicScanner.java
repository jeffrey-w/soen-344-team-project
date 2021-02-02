package scanner;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.isWhitespace;

public class PicScanner extends AbstractScanner {

    private static final Map<String, Token> RESERVED_WORDS = new HashMap<>();

    static {
        RESERVED_WORDS.put("BEGIN", tokenOf(Token.TokenType.BEGIN));
        RESERVED_WORDS.put("END", tokenOf(Token.TokenType.END));
        RESERVED_WORDS.put("INT", tokenOf(Token.TokenType.INT));
        RESERVED_WORDS.put("SET", tokenOf(Token.TokenType.SET));
        RESERVED_WORDS.put("BOOL", tokenOf(Token.TokenType.BOOL));
        RESERVED_WORDS.put("OR", tokenOf(Token.TokenType.OR));
        RESERVED_WORDS.put("INC", tokenOf(Token.TokenType.INC));
        RESERVED_WORDS.put("DEC", tokenOf(Token.TokenType.DEC));
        RESERVED_WORDS.put("ROL", tokenOf(Token.TokenType.ROL));
        RESERVED_WORDS.put("ROR", tokenOf(Token.TokenType.ROR));
        RESERVED_WORDS.put("IF", tokenOf(Token.TokenType.IF));
        RESERVED_WORDS.put("THEN", tokenOf(Token.TokenType.THEN));
        RESERVED_WORDS.put("ELSE", tokenOf(Token.TokenType.ELSE));
        RESERVED_WORDS.put("ELSIF", tokenOf(Token.TokenType.ELSIF));
        RESERVED_WORDS.put("WHILE", tokenOf(Token.TokenType.WHILE));
        RESERVED_WORDS.put("DO", tokenOf(Token.TokenType.DO));
        RESERVED_WORDS.put("REPEAT", tokenOf(Token.TokenType.REPEAT));
        RESERVED_WORDS.put("UNTIL", tokenOf(Token.TokenType.UNTIL));
        RESERVED_WORDS.put("CONST", tokenOf(Token.TokenType.CONST));
        RESERVED_WORDS.put("PROCEDURE", tokenOf(Token.TokenType.PROCED));
        RESERVED_WORDS.put("RETURN", tokenOf(Token.TokenType.RETURN));
        RESERVED_WORDS.put("MODULE", tokenOf(Token.TokenType.MODULE));
    }

    public PicScanner(String source) {
        super(source);
    }

    @Override
    Token scanNumber() {
        if (previousCharacter() == '$') {
            consume(); // Do not include '$'.
            return hexNumber();
        }
        while (isDigit(peekCharacter())) {
            nextCharacter();
        }
        return tokenOf(Token.TokenType.NUMBER, Integer.parseInt(currentLexeme()));
    }

    @Override
    Token scanIdentifier() {
        if (!isAlpha(previousCharacter())) {
            return tokenOf(Token.TokenType.NULL); // TODO error
        }
        while (isAlphanumeric(peekCharacter())) {
            nextCharacter();
        }
        String lexeme = currentLexeme();
        return RESERVED_WORDS.getOrDefault(lexeme, tokenOf(Token.TokenType.IDENT, lexeme));
    }

    private Token hexNumber() {
        while (isHexDigit(peekCharacter())) {
            nextCharacter();
        }
        return tokenOf(Token.TokenType.NUMBER, Integer.parseInt(currentLexeme(), 0x10));
    }

    private boolean isHexDigit(char character) {
        return isDigit(character) || character >= 'A' && character <= 'F';
    }

    private boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    private boolean isAlpha(char character) {
        return character >= 'A' && character <= 'z';
    }

    private boolean isAlphanumeric(char character) {
        return isDigit(character) || isAlpha(character);
    }

}
