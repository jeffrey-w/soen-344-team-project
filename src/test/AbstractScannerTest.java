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

    static final String Source = "*/+-~&=#>>=<<=.,::=!?();";
    static final List<Token> ExpectedSequence = List.of(
            Token.AST,
            Token.SLASH,
            Token.PLUS,
            Token.MINUS,
            Token.NOT,
            Token.AND,
            Token.EQL,
            Token.NEQ,
            Token.GTR,
            Token.GEQ,
            Token.LSS,
            Token.LEQ,
            Token.PERIOD,
            Token.COMMA,
            Token.COLON,
            Token.BECOMES,
            Token.OP,
            Token.QUERY,
            Token.LPAREN,
            Token.RPAREN,
            Token.SEMICOLON,
            Token.EOF
    );

    @Test
    void getToken() {
        IScanner scanner = new MockScanner(Source);
        for (Token expected : ExpectedSequence) {
            assertEquals(expected, scanner.getToken());
        }
    }

}