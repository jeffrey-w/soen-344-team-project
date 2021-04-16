package main.picl.parser;

public final class MemoryAddressValue extends AbstractValue{

    public MemoryAddressValue(int payload) {
        super(payload);
    }

    @Override
    int validatePayload(int payload) {
        if (payload < 0xC || payload >= 0x40) {
            throw new Error(); // TODO need picl error
        }
        return payload;
    }
    
}
