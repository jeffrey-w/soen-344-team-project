package main.scanner;

/**
 * The {@code Position} class represents a pair of indices that together specify a location, or contiguous range of
 * locations, in a linearly traversable data structure.
 */
public final class Position {

    private int start;
    private int current;
    private int line;
    private int column;

    public Position() {
        line = 1;
        column = 1;
    }

    /**
     * Provides the index at the beginning of this {@code Position}.
     *
     * @return the first index in the range specified by this {@code Position}
     */
    public int getStart() {
        return start;
    }

    /**
     * Provides the index at the end of this {@code Position}.
     *
     * @return the last index in the range specified by this {@code Position}
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Provides the index at the end of this {@code Position} and increments it afterwards.
     *
     * @return the last index in the range specified by this {@code Position}
     */
    public int advance() {
        return current++;
    }

    /**
     * TODO
     * @return
     */
    public int getLine() {
        return line;
    }

    /**
     * TODO
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * TODO
     */
    public void incrementLine() {
        line++;
    }

    /**
     * TODO
     */
    public void incrementColumn() {
        column++;
    }

    /**
     * TODO
     */
    public void resetColumn() {
        column = 1;
    }

    /**
     * Induces this {@code Position} to specify a single location at the current value of its last index.
     */
    public void collapse() {
        start = current;
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }
}
