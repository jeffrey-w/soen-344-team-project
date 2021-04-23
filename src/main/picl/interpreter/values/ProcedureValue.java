package main.picl.interpreter.values;

public class ProcedureValue extends AbstractValue {

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
    public boolean isImmediate() {
        return false;
    }

    @Override
    public boolean isAddress() {
        return false;
    }

}
