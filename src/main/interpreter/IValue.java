package main.interpreter;

/**
 * The interface Value.
 */
public interface IValue {

    /**
     * Gets type.
     *
     * @return the type
     */
    Enum<?> getType();

    /**
     * Gets payload.
     *
     * @return the payload
     */
    int getPayload();

    /**
     * Is immediate boolean.
     *
     * @return the boolean
     */
    boolean isImmediate();

    /**
     * Is address boolean.
     *
     * @return the boolean
     */
    boolean isAddress();
}
