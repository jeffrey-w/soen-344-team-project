package scanner;

import java.util.Objects;

abstract class AbstractScanner implements IScanner {

    static Token tokenOf(Token.TokenType type) {
        return new Token(type);
    }

    static Token tokenOf(Token.TokenType type, Object value) {
        return new ValueToken(type, value);
    }

    private int start, current;
    private final String source;

    AbstractScanner(String source) {
        this.source = Objects.requireNonNull(source);
    }

    @Override
    public Token getToken() {
        skipWhitespace();
        start = current;
        switch (nextCharacter()) {
            case '\0':
                return tokenOf(Token.TokenType.EOF);
            case '*':
                return tokenOf(Token.TokenType.AST);
            case '/':
                return tokenOf(Token.TokenType.SLASH);
            case '+':
                return tokenOf(Token.TokenType.PLUS);
            case '-':
                return tokenOf(Token.TokenType.MINUS);
            case '~':
                return tokenOf(Token.TokenType.NOT);
            case '&':
                return tokenOf(Token.TokenType.AND);
            case '=':
                return tokenOf(Token.TokenType.EQL);
            case '#':
                return tokenOf(Token.TokenType.NEQ);
            case '>':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return tokenOf(Token.TokenType.GEQ);
                }
                return tokenOf(Token.TokenType.GTR);
            case '<':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return tokenOf(Token.TokenType.LEQ);
                }
                return tokenOf(Token.TokenType.LSS);
            case '.':
                return tokenOf(Token.TokenType.PERIOD);
            case ',':
                return tokenOf(Token.TokenType.COMMA);
            case ':':
                if (peekCharacter() == '=') {
                    nextCharacter();
                    return tokenOf(Token.TokenType.BECOMES);
                }
                return tokenOf(Token.TokenType.COLON);
            case '!':
                return tokenOf(Token.TokenType.OP);
            case '?':
                return tokenOf(Token.TokenType.QUERY);
            case '(':
                return tokenOf(Token.TokenType.LPAREN);
            case ')':
                return tokenOf(Token.TokenType.RPAREN);
            case ';':
                return tokenOf(Token.TokenType.SEMICOLON);
            case '$':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return scanNumber();
            default:
                return scanIdentifier();
        }
    }

    abstract void skipWhitespace();

    abstract Token scanNumber();

    abstract Token scanIdentifier();

    char previousCharacter() {
        return source.charAt(current - 1);
    }

    char nextCharacter() {
        if (current >= source.length()) {
            return '\0';
        }
        return source.charAt(current++);
    }

    char peekCharacter() {
        if (current >= source.length()) {
            return '\0';
        }
        return source.charAt(current);
    }

    void consume() {
        start++;
    }

    String currentLexeme() {
        return source.substring(start, current);
    }

}
