package test;

import main.scanner.IScanner;
import main.picl.PICScanner;
import main.tokens.IToken;
import main.picl.PICToken;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PICScannerTest {

    static final String Source = "*/+-~&=#>>=<<=.,::=!?();";
    static final List<IToken> ExpectedSequence = List.of(
            new PICToken(PICToken.TokenType.AST),
            new PICToken(PICToken.TokenType.SLASH),
            new PICToken(PICToken.TokenType.PLUS),
            new PICToken(PICToken.TokenType.MINUS),
            new PICToken(PICToken.TokenType.NOT),
            new PICToken(PICToken.TokenType.AND),
            new PICToken(PICToken.TokenType.EQL),
            new PICToken(PICToken.TokenType.NEQ),
            new PICToken(PICToken.TokenType.GTR),
            new PICToken(PICToken.TokenType.GEQ),
            new PICToken(PICToken.TokenType.LSS),
            new PICToken(PICToken.TokenType.LEQ),
            new PICToken(PICToken.TokenType.PERIOD),
            new PICToken(PICToken.TokenType.COMMA),
            new PICToken(PICToken.TokenType.COLON),
            new PICToken(PICToken.TokenType.BECOMES),
            new PICToken(PICToken.TokenType.OP),
            new PICToken(PICToken.TokenType.QUERY),
            new PICToken(PICToken.TokenType.LPAREN),
            new PICToken(PICToken.TokenType.RPAREN),
            new PICToken(PICToken.TokenType.SEMICOLON),
            new PICToken(PICToken.TokenType.EOF)
    );

    @Test
    public void getToken() {
        IScanner scanner = new PICScanner(Source);
        for (IToken expected : ExpectedSequence) {
            assertEquals(expected, scanner.getToken());
        }
    }

}