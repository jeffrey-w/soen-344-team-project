package main.interpreter;

public interface IValue {

    Enum<?> getType();
    int getPayload();
    boolean isImmediate();
    boolean isAddress();
}
