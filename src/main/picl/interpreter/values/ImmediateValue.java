package main.picl.interpreter.values;

import main.picl.scanner.Token;

public final class ImmediateValue extends AbstractValue {

    public ImmediateValue(int payload) {
        super(payload);
    }

    @Override
    int validatePayload(int payload) {
        if (payload < 0 || payload >= 0x100) {
            throw new Error(); // TODO need picl error
        }
        return payload;
    }

    @Override
    public Enum<?> getType() {
        return Token.TokenType.INT; // TODO this may not always be the case
    }

    @Override
    public boolean isImmediate() {
        return true;
    }

    @Override
    public boolean isAddress() {
        return false;
    }
    

}