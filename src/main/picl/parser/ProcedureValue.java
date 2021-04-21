package main.picl.parser;

import main.parser.Environment;

public class ProcedureValue extends AbstractValue {

    private final Environment locals;

    public ProcedureValue(final int payload, final Environment locals) {
        super(payload);
        this.locals = locals;
    }

    @Override
    int validatePayload(final int payload) {
        if (payload < 0) { // TODO less than 1
            throw new Error(); // TODO need picl errof
        }
        return payload;
    }
}
