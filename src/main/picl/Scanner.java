package main.picl;

import main.scanner.AbstractScanner;
import main.tokens.IToken;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Scanner} provides an concrete implementation of the {@code IScanner} interface. It reads source files
 * written in the PICL programming language created by Niklaus Wirth.
 */
public class Scanner extends AbstractScanner {

    private static final Map<String, Token.TokenType> KEYWORDS = new HashMap<>();

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

    /**
     * Creates a new {@code Scanner} to read the specified {@code source} file.
     *
     * @param sourceFileContent the source file content that will be read by the returned {@code Scanner}
     * @throws NullPointerException if the specified {@code source} is {@code null}
     */
    public Scanner(String sourceFileContent) {
        super(sourceFileContent);
    }

    @Override
    protected IToken scanNumber() {
        if (previousCharacter() == '$') {
            return scanHexNumber();
        }
        while (isDigit(peekCharacter())) { // TODO error on more than 3 digits
            nextCharacter();
        }
        return new Token(Token.TokenType.NUMBER, currentPosition(), Integer.parseInt(currentLexeme()));
    }

    private IToken scanHexNumber() {
        do {
            nextCharacter();
        } while (isHexDigit(peekCharacter())); // TODO error on more than 2 digits
        return new Token(Token.TokenType.NUMBER, currentPosition(),
                Integer.parseInt(currentLexeme().substring(1), 0x10));
    }

    private boolean isHexDigit(char character) {
        return isDigit(character) || (character >= 'A' && character <= 'F');
    }

    private boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    @Override
    protected IToken scanIdentifier() {
        // TODO error if peekCharacter is not alpha
        while (isAlphanumeric(peekCharacter())) {
            nextCharacter();
        }
        String lexeme = currentLexeme();
        if (KEYWORDS.containsKey(lexeme)) {
            return new Token(KEYWORDS.get(lexeme), currentPosition());
        } else {
            return new Token(Token.TokenType.IDENTIFIER, currentPosition(), lexeme);
        }
    }

    private boolean isAlphabetic(char character) {
        return character >= 'A' && character <= 'z';
    }

    private boolean isAlphanumeric(char character) {
        return isDigit(character) || isAlphabetic(character);
    }

}
