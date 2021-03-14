package test;

import main.errors.Error;
import main.scanner.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * The type Error test.
 */
public class ErrorTest {

    private static final String MESSAGE = "test";
    private static final Position POSITION = new Position();

    private Error error;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        error = new Error(MESSAGE, POSITION);
    }

    /**
     * Gets message.
     */
    @Test
    public void getMessage() {
        assertSame(MESSAGE, error.getMessage());
    }

    /**
     * Gets position.
     */
    @Test
    public void getPosition() {
        assertNotSame(POSITION, error.getPosition());
    }

}