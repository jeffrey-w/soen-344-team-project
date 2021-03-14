package test;

import main.scanner.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The type Position test.
 */
public class PositionTest {

    private Position position;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        position = new Position();
    }

    /**
     * Gets line.
     */
    @Test
    public void getLine() {
        assertEquals(1, position.getLine());
    }

    /**
     * Gets column.
     */
    @Test
    public void getColumn() {
        assertEquals(1, position.getColumn());
    }

    /**
     * Increment line.
     */
    @Test
    public void incrementLine() {
        position.incrementColumn();
        position.incrementLine();
        assertEquals(2, position.getLine());
        assertEquals(1, position.getColumn());
    }

    /**
     * Increment column.
     */
    @Test
    public void incrementColumn() {
        position.incrementColumn();
        assertEquals(2, position.getColumn());
    }

}