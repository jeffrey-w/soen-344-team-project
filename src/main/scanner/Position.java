package main.scanner;

/**
 * The {@code Position} class represents a pair of indices that together specify a location, or contiguous range of
 * locations, in a linearly traversable data structure.
 */
public final class Position {

    private int start;
    private int current;

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
     * Induces this {@code Position} to specify a single location at the current value of its last index.
     */
    public void collapse() {
        start = current;
    }

}
