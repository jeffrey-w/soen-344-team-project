package main.picl.interpreter.values;

public final class AddressValue extends AbstractValue {

    private final Enum<?> type;

    public AddressValue(int payload, Enum<?> type) {
        super(payload);
        this.type = type; // TODO validate this
    }

    @Override
    int validatePayload(int payload) {
        if (payload < 0x5 || payload >= 0x40) {
            throw new Error(); // TODO need picl error
        }
        return payload;
    }

    public Enum<?> getType() {
        return type;
    }

    @Override
    public boolean isImmediate() {
        return false;
    }

    @Override
    public boolean isAddress() {
        return true;
    }
    
}
