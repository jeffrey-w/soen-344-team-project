package test;

import main.scanner.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {

    private Position position;

    @Before
    public void setUp() {
        position = new Position();
    }

    @Test
    public void getLine() {
        assertEquals(1, position.getLine());
    }

    @Test
    public void getColumn() {
        assertEquals(1, position.getColumn());
    }

    @Test
    public void incrementLine() {
        position.incrementColumn();
        position.incrementLine();
        assertEquals(2, position.getLine());
        assertEquals(1, position.getColumn());
    }

    @Test
    public void incrementColumn() {
        position.incrementColumn();
        assertEquals(2, position.getColumn());
    }

}