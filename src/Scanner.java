import java.util.*;
import java.util.regex.Pattern;

public class Scanner {

    private static final Pattern WHITESPACE_CHARS = Pattern.compile("\\s");
    private static final Pattern ALPHA_CHARS = Pattern.compile("[a-zA-Z]");
    private static final Pattern HEX_CHARS = Pattern.compile("[A-F]");
    private static final Pattern DIGIT_CHARS = Pattern.compile("\\d");
    private static final Pattern WORD_CHARS = Pattern.compile("[a-zA-Z\\d]");
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

    private static Token tokenOf(Token.TokenType type) {
        return new Token(type);
    }

    private static Token tokenOf(Token.TokenType type, Object value) {
        return new ValueToken(type, value);
    }

    private int start, current;
    private final String source;

    public Scanner(String source) {
        this.source = Objects.requireNonNull(source);
    }

    public List<Token> scan() {
        Token token;
        List<Token> tokens = new ArrayList<>();
        do {
            token = nextToken();
            tokens.add(token);
            start = current;
        } while (token.getType() != Token.TokenType.EOF);
        return tokens;
    }

    private Token nextToken() {
        skipWhitespace();
        char character = nextChar();
        switch (character) {
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
            case '|':
                return tokenOf(Token.TokenType.OR);
            case '=':
                return tokenOf(Token.TokenType.EQL);
            case '#':
                return tokenOf(Token.TokenType.NEQ);
            case '>':
                if (lookAhead() == '=') {
                    nextChar();
                    return tokenOf(Token.TokenType.GEQ);
                }
                return tokenOf(Token.TokenType.GTR);
            case '<':
                if (lookAhead() == '=') {
                    nextChar();
                    return tokenOf(Token.TokenType.LEQ);
                }
                return tokenOf(Token.TokenType.LSS);
            case '.':
                return tokenOf(Token.TokenType.PERIOD);
            case ',':
                return tokenOf(Token.TokenType.COMMA);
            case ':':
                if (lookAhead() == '=') {
                    nextChar();
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
                return hexNumber();
            default:
                if (isDigit(character)) {
                    return number();
                }
                if (isAlpha(character)) {
                    return identifier();
                }
                return tokenOf(Token.TokenType.NULL); // TODO is this an error (i.e. unexpected character)?
        }
    }

    private void skipWhitespace() {
        while (isWhitespace()) {
            nextChar();
        }
        start = current;
    }

    private char nextChar() {
        if (current >= source.length()) {
            return '\0';
        }
        return source.charAt(current++);
    }

    private char lookAhead() {
        if (current >= source.length()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private Token hexNumber() {
        start++; // Consume '$' character.
        while (isHexDigit(lookAhead())) { // TODO error if more than 2 digits
            nextChar();
        }
        return tokenOf(Token.TokenType.NUMBER, Integer.parseInt(currentLexeme(), 0x10));
    }

    private Token number() {
        while (isDigit(lookAhead())) { // TODO error if more than 3 digits
            nextChar();
        }
        return tokenOf(Token.TokenType.NUMBER, Double.parseDouble(currentLexeme()));
    }

    private Token identifier() {
        while (isAlphanumeric(lookAhead())) {
            nextChar();
        }
        String lexeme = currentLexeme();
        return RESERVED_WORDS.getOrDefault(lexeme, tokenOf(Token.TokenType.IDENT, lexeme));
    }

    private boolean isWhitespace() {
        return WHITESPACE_CHARS.matcher(String.valueOf(lookAhead())).matches();
    }

    private boolean isHexDigit(char c) {
        return isDigit(c) || HEX_CHARS.matcher(String.valueOf(c)).matches();
    }

    private boolean isDigit(char c) {
        return DIGIT_CHARS.matcher(String.valueOf(c)).matches();
    }

    private boolean isAlpha(char c) {
        return ALPHA_CHARS.matcher(String.valueOf(c)).matches();
    }

    private boolean isAlphanumeric(char c) {
        return WORD_CHARS.matcher(String.valueOf(c)).matches();
    }

    private String currentLexeme() {
        return source.substring(start, current);
    }

}
