package main.picl.parser;

import main.parser.IValue;

abstract class AbstractValue implements IValue {
    
    private final int payload;

    AbstractValue(int payload) {
        this.payload = validatePayload(payload);
    }

    abstract int validatePayload(int payload);

    @Override
    public int getPayload() {
        return payload;
    }
    
}
