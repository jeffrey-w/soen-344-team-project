package main.picl.parser;

public final class LiteralValue extends AbstractValue {

    public LiteralValue(int payload) {
        super(payload);
    }

    @Override
    int validatePayload(int payload) {
        if (payload < 0 || payload >= 0x100) {
            throw new Error(); // TODO need picl error
        }
        return payload;
    }

}