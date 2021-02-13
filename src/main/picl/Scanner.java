package main.picl;

import main.scanner.AbstractScanner;
import main.scanner.ISymbolTable;
import main.tokens.IToken;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * The {@code Scanner} provides an concrete implementation of the {@code IScanner} interface. It reads source files
 * written in the PICL programming language created by Niklaus Wirth.
 */
public class Scanner extends AbstractScanner {

    private static final Set<Character> OPERATORS = new HashSet<>(
            Arrays.asList('*', '/', '+', '-', '~', '&', '=', '#', '>', '<', '.', ',', ':', '!', '?', '(', ')', ';',
                    '\0'));

    /**
     * Creates a new {@code Scanner} to read the specified {@code source} file.
     *
     * @param sourceFileContent the source file content that will be read by the returned {@code Scanner}
     * @param keywords the reserved words accepted by the returned {@code Scanner}
     * @throws NullPointerException if the specified {@code source} or {@code keywords} are {@code null}
     */
    public Scanner(String sourceFileContent, ISymbolTable keywords) {
        super(sourceFileContent, keywords);
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
        Enum<?> type = getKeywords().getTypeFor(lexeme);
        if (type == Token.TokenType.IDENTIFIER) {
            return new Token((Token.TokenType) type, currentPosition(), lexeme);
        } else {
            return new Token((Token.TokenType) type, currentPosition());
        }
    }

    private boolean isAlphabetic(char character) {
        return character >= 'A' && character <= 'z';
    }

    private boolean isAlphanumeric(char character) {
        return isDigit(character) || isAlphabetic(character);
    }

}
