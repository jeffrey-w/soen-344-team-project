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

    private static final Map<String, IToken> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("BEGIN", new Token(Token.TokenType.BEGIN));
        KEYWORDS.put("END", new Token(Token.TokenType.END));
        KEYWORDS.put("INT", new Token(Token.TokenType.INT));
        KEYWORDS.put("SET", new Token(Token.TokenType.SET));
        KEYWORDS.put("BOOL", new Token(Token.TokenType.BOOL));
        KEYWORDS.put("OR", new Token(Token.TokenType.OR));
        KEYWORDS.put("INC", new Token(Token.TokenType.INC));
        KEYWORDS.put("DEC", new Token(Token.TokenType.DEC));
        KEYWORDS.put("ROL", new Token(Token.TokenType.ROL));
        KEYWORDS.put("ROR", new Token(Token.TokenType.ROR));
        KEYWORDS.put("IF", new Token(Token.TokenType.IF));
        KEYWORDS.put("THEN", new Token(Token.TokenType.THEN));
        KEYWORDS.put("ELSE", new Token(Token.TokenType.ELSE));
        KEYWORDS.put("ELSIF", new Token(Token.TokenType.ELSIF));
        KEYWORDS.put("WHILE", new Token(Token.TokenType.WHILE));
        KEYWORDS.put("DO", new Token(Token.TokenType.DO));
        KEYWORDS.put("REPEAT", new Token(Token.TokenType.REPEAT));
        KEYWORDS.put("UNTIL", new Token(Token.TokenType.UNTIL));
        KEYWORDS.put("CONST", new Token(Token.TokenType.CONST));
        KEYWORDS.put("PROCEDURE", new Token(Token.TokenType.PROCED));
        KEYWORDS.put("RETURN", new Token(Token.TokenType.RETURN));
        KEYWORDS.put("MODULE", new Token(Token.TokenType.MODULE));
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
            return hexNumber();
        }
        while (isDigit(peekCharacter())) { // TODO error on more than 3 digits
            nextCharacter();
        }
        String lexeme = currentLexeme();
        return new Token(Token.TokenType.NUMBER, lexeme, Integer.parseInt(lexeme));
    }

    private IToken hexNumber() {
        do {
            nextCharacter();
        } while (isHexDigit(peekCharacter())); // TODO error on more than 2 digits
        String lexeme = currentLexeme();
        return new Token(Token.TokenType.NUMBER, lexeme, Integer.parseInt(lexeme.substring(1), 0x10));
    }

    private boolean isHexDigit(char character) {
        return isDigit(character) || ( character >= 'A' && character <= 'F' );
    }

    private boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    private boolean isAlphabet(char character) {
        return character >= 'A' && character <= 'z';
    }

    @Override
    protected IToken scanIdentifier() {
        // TODO error if peekCharacter is not alpha
        while (isAlphanumeric(peekCharacter())) {
            nextCharacter();
        }
        String lexeme = currentLexeme();
        return KEYWORDS.getOrDefault(lexeme, new Token(Token.TokenType.IDENT, lexeme, lexeme));
    }

    private boolean isAlphanumeric(char character) {
        return isDigit(character) || isAlphabet(character);
    }

}
