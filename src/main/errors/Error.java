package main.errors;

import main.scanner.Position;

import java.util.Objects;

public final class Error {

    private final String message;
    private final Position position;

    public Error(String message, Position position) {
        this.message = Objects.requireNonNull(message);
        this.position = Objects.requireNonNull(position).clone(); // Create a defensive copy since Position is mutable.
    }

    public String getMessage() {
        return message;
    }

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
