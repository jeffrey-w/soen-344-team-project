package main.picl.scanner;

import main.scanner.AbstractScanner;
import main.scanner.IToken;

import java.util.*;

/**
 * The {@code Scanner} provides an concrete implementation of the {@code IScanner} interface. It reads source files
 * written in the PICL programming language created by Niklaus Wirth.
 */
public class Scanner extends AbstractScanner {

    private static final Set<Character> OPERATORS = new HashSet<>(
            Arrays.asList('*', '/', '+', '-', '~', '&', '=', '#', '>', '<', '.', ',', ':', '!', '?', '(', ')', ';',
                    '\0'));

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
     * Creates a new {@code Scanner} to read the specified {@code sourceFileContent} file.
     *
     * @param sourceFileContent the source file that will be read by the returned {@code Scanner}
     * @throws NullPointerException if the specified {@code sourceFileContent} is {@code null}
     */
    public Scanner(String sourceFileContent) {
        super(sourceFileContent);
    }

    @Override
    public boolean isOperator() {
        return OPERATORS.contains(peekCharacter());
    }

    @Override
    public IToken scanOperator() {
        switch (nextCharacter()) {
            case '\0':
                return new Token(Token.TokenType.EOF, currentPosition());
            case '*':
                return new Token(Token.TokenType.AST, currentPosition());
            case '/':
                return new Token(Token.TokenType.SLASH, currentPosition());
            case '+':
                return new Token(Token.TokenType.PLUS, currentPosition());
            case '-':
                return new Token(Token.TokenType.MINUS, currentPosition());
            case '~':
                return new Token(Token.TokenType.NOT, currentPosition());
            case '&':
                return new Token(Token.TokenType.AND, currentPosition());
            case '=':
                return new Token(Token.TokenType.EQL, currentPosition());
            case '#':
                return new Token(Token.TokenType.NEQ, currentPosition());
            case '>':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.GEQ, currentPosition());
                }
                return new Token(Token.TokenType.GTR, currentPosition());
            case '<':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.LEQ, currentPosition());
                }
                return new Token(Token.TokenType.LSS, currentPosition());
            case '.':
                return new Token(Token.TokenType.PERIOD, currentPosition());
            case ',':
                return new Token(Token.TokenType.COMMA, currentPosition());
            case ':':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return new Token(Token.TokenType.BECOMES, currentPosition());
                }
                return new Token(Token.TokenType.COLON, currentPosition());
            case '!':
                return new Token(Token.TokenType.OP, currentPosition());
            case '?':
                return new Token(Token.TokenType.QUERY, currentPosition());
            case '(':
                return new Token(Token.TokenType.LEFT_PARENTHESIS, currentPosition());
            case ')':
                return new Token(Token.TokenType.RIGHT_PARENTHESIS, currentPosition());
            case ';':
                return new Token(Token.TokenType.SEMICOLON, currentPosition());
            default:
                throw new NoSuchElementException("The current character is not an operator");
        }
    }

    @Override
    protected boolean isNumeric() {
        return isDigit(peekCharacter()) || peekCharacter() == '$';
    }

    @Override
    protected IToken scanNumber() {
        if (peekCharacter() == '$') {
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
            return new Token(KEYWORDS.get(lexeme), currentPosition(), null);
        }
        return new Token(Token.TokenType.IDENTIFIER, currentPosition(), lexeme);
    }

    private boolean isAlphabetic(char character) {
        return character >= 'A' && character <= 'z';
    }

    private boolean isAlphanumeric(char character) {
        return isDigit(character) || isAlphabetic(character);
    }

}
