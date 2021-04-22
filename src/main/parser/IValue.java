package main.parser;

public interface IValue {

    int getPayload();
    boolean isImmediate();
    boolean isAddress();
}
