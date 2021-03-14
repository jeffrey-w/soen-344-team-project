package test;

import main.picl.scanner.Token;
import main.scanner.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Token test.
 */
public class TokenTest {

    private static final Position POSITION = new Position();

    /**
     * The Token.
     */
    Token token;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        token = new Token(Token.TokenType.EOF, POSITION);
    }

    /**
     * Gets type.
     */
    @Test
    public void getType() {
        assertNotNull(token.getType());
    }

    /**
     * Gets position.
     */
    @Test
    public void getPosition() {
        assertNotSame(POSITION, token.getPosition());
    }

    /**
     * Gets value.
     */
    @Test
    public void getValue() {
        assertNull(token.getValue());
    }

    /**
     * Is eof.
     */
    @Test
    public void isEOF() {
        assertTrue(token.isEOF());
    }

}