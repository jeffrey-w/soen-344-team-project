package main.picl.interpreter.values;

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
    public boolean isImmediate() {
        return true;
    }

    @Override
    public boolean isAddress() {
        return false;
    }
    

}