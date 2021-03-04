package test;

import main.picl.scanner.Token;
import main.scanner.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private static final Position POSITION = new Position();

    Token token;

    @Before
    public void setUp() {
        token = new Token(Token.TokenType.EOF, POSITION);
    }

    @Test
    public void getType() {
        assertNotNull(token.getType());
    }

    @Test
    public void getPosition() {
        assertNotSame(POSITION, token.getPosition());
    }

    @Test
    public void getValue() {
        assertNull(token.getValue());
    }

    @Test
    public void isEOF() {
        assertTrue(token.isEOF());
    }

}