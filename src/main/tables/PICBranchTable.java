package main.tables;

import main.scanner.Position;
import main.tokens.IToken;
import main.tokens.PICToken;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * The {@code PICBranchTable} class defines the reserved symbols for the PIC programming language, and specifies
 * operations for lexing them.
 */
public class PICBranchTable implements IBranchTable {

    private static final Map<Character, BiFunction<Character, Position, IToken>> SYMBOLS = new HashMap<>();

    static {
        SYMBOLS.put('\0', (character, position) -> new PICToken(PICToken.TokenType.EOF));
        SYMBOLS.put('*', (character, position) -> new PICToken(PICToken.TokenType.AST));
        SYMBOLS.put('/', (character, position) -> new PICToken(PICToken.TokenType.SLASH));
        SYMBOLS.put('+', (character, position) -> new PICToken(PICToken.TokenType.PLUS));
        SYMBOLS.put('-', (character, position) -> new PICToken(PICToken.TokenType.MINUS));
        SYMBOLS.put('~', (character, position) -> new PICToken(PICToken.TokenType.NOT));
        SYMBOLS.put('&', (character, position) -> new PICToken(PICToken.TokenType.AND));
        SYMBOLS.put('=', (character, position) -> new PICToken(PICToken.TokenType.EQL));
        SYMBOLS.put('#', (character, position) -> new PICToken(PICToken.TokenType.NEQ));
        SYMBOLS.put('>', ((character, position) -> {
            if (character == '=') {
                position.advance();
                return new PICToken(PICToken.TokenType.GEQ);
            }
            return new PICToken(PICToken.TokenType.GTR);
        }));
        SYMBOLS.put('<', (character, position) -> {
            if (character == '=') {
                position.advance();
                return new PICToken(PICToken.TokenType.LEQ);
            }
            return new PICToken(PICToken.TokenType.LSS);
        });
        SYMBOLS.put('.', (character, position) -> new PICToken(PICToken.TokenType.PERIOD));
        SYMBOLS.put(',', (character, position) -> new PICToken(PICToken.TokenType.COMMA));
        SYMBOLS.put(':', (character, position) -> {
            if (character == '=') {
                position.advance();
                return new PICToken(PICToken.TokenType.BECOMES);
            }
            return new PICToken(PICToken.TokenType.COLON);
        });
        SYMBOLS.put('!', (character, position) -> new PICToken(PICToken.TokenType.OP));
        SYMBOLS.put('?', (character, position) -> new PICToken(PICToken.TokenType.QUERY));
        SYMBOLS.put('(', (character, position) -> new PICToken(PICToken.TokenType.LPAREN));
        SYMBOLS.put(')', (character, position) -> new PICToken(PICToken.TokenType.RPAREN));
        SYMBOLS.put(';', (character, position) -> new PICToken(PICToken.TokenType.SEMICOLON));

    }

    @Override
    public boolean isSymbol(char character) {
        return SYMBOLS.containsKey(character);
    }

    @Override
    public IToken tokenFor(char character, char lookahead, Position position) {
        // TODO should we bother catching null pointer exceptions?
        return SYMBOLS.get(character).apply(lookahead, position);
    }

}
