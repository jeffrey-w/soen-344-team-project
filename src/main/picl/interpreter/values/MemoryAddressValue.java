package main.picl.interpreter.values;

public final class MemoryAddressValue extends AbstractValue {

    private final Enum<?> type;

    public MemoryAddressValue(int payload, Enum<?> type) {
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAddress() {
        // TODO Auto-generated method stub
        return true;
    }
    
}
