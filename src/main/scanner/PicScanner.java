package main.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code PicScanner} provides an instantiable implementation of the {@code main.scanner.IScanner} interface. It reads
 * source files written in the PIC language created by Niklaus Wirth.
 */
public class PicScanner extends AbstractScanner {

    private static final Map<String, Token> RESERVED_WORDS = new HashMap<>();

    static {
        RESERVED_WORDS.put("BEGIN", Token.BEGIN);
        RESERVED_WORDS.put("END", Token.END);
        RESERVED_WORDS.put("INT", Token.INT);
        RESERVED_WORDS.put("SET", Token.SET);
        RESERVED_WORDS.put("BOOL", Token.BOOL);
        RESERVED_WORDS.put("OR", Token.OR);
        RESERVED_WORDS.put("INC", Token.INC);
        RESERVED_WORDS.put("DEC", Token.DEC);
        RESERVED_WORDS.put("ROL", Token.ROL);
        RESERVED_WORDS.put("ROR", Token.ROR);
        RESERVED_WORDS.put("IF", Token.IF);
        RESERVED_WORDS.put("THEN", Token.THEN);
        RESERVED_WORDS.put("ELSE", Token.ELSE);
        RESERVED_WORDS.put("ELSIF", Token.ELSIF);
        RESERVED_WORDS.put("WHILE", Token.WHILE);
        RESERVED_WORDS.put("DO", Token.DO);
        RESERVED_WORDS.put("REPEAT", Token.REPEAT);
        RESERVED_WORDS.put("UNTIL", Token.UNTIL);
        RESERVED_WORDS.put("CONST", Token.CONST);
        RESERVED_WORDS.put("PROCEDURE", Token.PROCED);
        RESERVED_WORDS.put("RETURN", Token.RETURN);
        RESERVED_WORDS.put("MODULE", Token.MODULE);
    }

    /**
     * Creates a new {@code PicScanner} to read the specified {@code source} file.
     *
     * @param source the source file that will be read by the returned {@code PicScanner}
     * @throws NullPointerException if the specified {@code source} is {@code null}
     */
    public PicScanner(String source) {
        super(source);
    }

    @Override
    protected Token scanNumber() {
        if (previousCharacter() == '$') {
            return hexNumber();
        }
        while (isDigit(peekCharacter())) { // TODO error on more than 3 digits
            nextCharacter();
        }
        return Token.NUMBER;
    }

    @Override
    protected Token scanIdentifier() {
        if (!isAlpha(previousCharacter())) {
            return Token.NULL; // TODO error
        }
        while (isAlphanumeric(peekCharacter())) {
            nextCharacter();
        }
        String lexeme = currentLexeme();
        return RESERVED_WORDS.getOrDefault(lexeme, Token.IDENT);
    }

    private Token hexNumber() {
        consume(); // Do not include '$'.
        while (isHexDigit(peekCharacter())) { // TODO error on more than 2 digits
            nextCharacter();
        }
        return Token.NUMBER;
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
