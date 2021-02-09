package test;

import main.scanner.IScanner;
import main.picl.Scanner;
import main.tokens.IToken;
import main.picl.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ScannerTest {

    static final String Source = "*/+-~&=#>>=<<=.,::=!?();";
    static final List<IToken> ExpectedSequence = List.of(
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
            new Token(Token.TokenType.SEMICOLON),
            new Token(Token.TokenType.EOF)
    );

    @Test
    public void getToken() {
        IScanner scanner = new Scanner(Source);
        for (IToken expected : ExpectedSequence) {
            assertEquals(expected, scanner.getToken());
        }
    }

}