package main.picl.interpreter.values;

import main.interpreter.IValue;

/**
 * The type Abstract value.
 */
abstract class AbstractValue implements IValue {
    
    private final int payload;

    /**
     * Instantiates a new Abstract value.
     *
     * @param payload the payload
     */
    AbstractValue(int payload) {
        this.payload = validatePayload(payload);
    }

    /**
     * Validate payload int.
     *
     * @param payload the payload
     * @return the int
     */
    abstract int validatePayload(int payload);

    @Override
    public int getPayload() {
        return payload;
    }
    
}
