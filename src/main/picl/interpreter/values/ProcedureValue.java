package main.picl.interpreter.values;

import main.picl.scanner.Token;

/**
 * The type Procedure value.
 */
public final class ProcedureValue extends AbstractValue {

    /**
     * Instantiates a new Procedure value.
     *
     * @param payload the payload
     */
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
