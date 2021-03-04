package main.picl.interpreter;

import main.picl.interpreter.decl.IDecl;

import java.util.HashMap;
import java.util.Map;

public final class Environment {

    private final Map<String, IDecl> symbols;

    public Environment() {
        symbols = new HashMap<>();
    }

    public void add(String identifier, IDecl declaration) {
        symbols.put(identifier, declaration);
    }

    public IDecl get(String identifier) {
        return symbols.get(identifier);
    }

}
