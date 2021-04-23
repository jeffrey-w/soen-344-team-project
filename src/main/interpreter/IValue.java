package main.interpreter;

public interface IValue {

    int getPayload();
    boolean isImmediate();
    boolean isAddress();
}
