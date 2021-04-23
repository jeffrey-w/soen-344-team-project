package main.picl.interpreter.values;

import main.picl.scanner.Token;

public final class ProcedureValue extends AbstractValue {

    public ProcedureValue(final int payload) {
        super(payload);
    }

    @Override
    int validatePayload(final int payload) {
        if (payload < 1) {
            throw new Error(); // TODO need picl error
        }
        return payload;
    }

    @Override
    public Enum<?> getType() {
        return Token.TokenType.PROCEDURE;
    }

    @Override
    public boolean isImmediate() {
        return false;
    }

    @Override
    public boolean isAddress() {
        return false;
    }

}
