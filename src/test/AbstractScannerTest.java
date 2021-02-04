package test;

import main.scanner.AbstractScanner;
import main.scanner.IScanner;
import main.scanner.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractScannerTest {

    static class MockScanner extends AbstractScanner {

        MockScanner(String source) {
            super(source);
        }

        @Override
        protected Token scanNumber() {
            return null;
        }

        @Override
        protected Token scanIdentifier() {
            return null;
        }

    }

    static final String Source = "*/+-~&=#>>=<<=.,::=!?()";
    static final List<Token> ExpectedSequence = List.of(
            new Token(Token.TokenType.AST),
            new Token(Token.TokenType.SLASH),
            new Token(Token.TokenType.PLUS),
            new Token(Token.TokenType.MINUS),
            new Token(Token.TokenType.NOT),
            new Token(Token.TokenType.AND),
            new Token(Token.TokenType.EQL),
            new Token(Token.TokenType.NEQ),
            new Token(Token.TokenType.GTR),
            new Token(Token.TokenType.GEQ),
            new Token(Token.TokenType.LSS),
            new Token(Token.TokenType.LEQ),
            new Token(Token.TokenType.PERIOD),
            new Token(Token.TokenType.COMMA),
            new Token(Token.TokenType.COLON),
            new Token(Token.TokenType.BECOMES),
            new Token(Token.TokenType.OP),
            new Token(Token.TokenType.QUERY),
            new Token(Token.TokenType.LPAREN),
            new Token(Token.TokenType.RPAREN),
            new Token(Token.TokenType.EOF)
    );

    @Test
    void getToken() {
        IScanner scanner = new MockScanner(Source);
        for (Token expected : ExpectedSequence) {
            assertEquals(expected, scanner.getToken());
        }
    }
}