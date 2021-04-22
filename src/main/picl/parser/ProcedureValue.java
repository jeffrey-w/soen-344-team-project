package main.picl.parser;

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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAddress() {
        // TODO Auto-generated method stub
        return false;
    }

}
