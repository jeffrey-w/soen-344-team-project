package main.scanner;

import java.util.Objects;

/**
 * The {@code Position} class represents a pair of indices that together specify a location in a text file in terms of a
 * line and column number.
 */
public final class Position implements Cloneable {

    private int line;
    private int column;

    /**
     * Creates a new {@code Position} at line and column 1.
     */
    public Position() {
        line = 1;
        column = 1;
    }

    @Override
    public Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e); // This should not happen since we're cloneable
        }
    }

    /**
     * Provides the line number that this {@code Position} is at.
     *
     * @return the line number of this {@code Position}
     */
    public int getLine() {
        return line;
    }

    /**
     * Provides the column number that this {@code Position} is at.
     *
     * @return the column number of this {@code Position}
     */
    public int getColumn() {
        return column;
    }

    /**
     * Increases the line number of this {@code Position} by one, and resets the column number to one.
     */
    public void incrementLine() {
        line++;
        column = 1;
    }

    /**
     * Increases the column number of this {@code Position} by one.
     */
    public void incrementColumn() {
        column++;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }
        Position position = (Position) obj;
        return line == position.line && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }

}
