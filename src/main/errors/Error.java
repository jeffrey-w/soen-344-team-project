package main.errors;

import main.scanner.Position;

import java.util.Objects;

/**
 * The type Error.
 */
public final class Error {

    private final String message;
    private final Position position;

    /**
     * Instantiates a new Error.
     *
     * @param message  the message
     * @param position the position
     */
    public Error(String message, Position position) {
        this.message = Objects.requireNonNull(message);
        this.position = Objects.requireNonNull(position).clone(); // Create a defensive copy since Position is mutable.
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Position getPosition() {
        return position.clone(); // Return a deep copy since Position is mutable.
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, position);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Error)) {
            return false;
        }
        Error error = (Error) obj;
        return message.equals(error.message) && position.equals(error.position);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
