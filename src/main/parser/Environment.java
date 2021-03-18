package main.parser;

import main.picl.interpreter.decl.IDecl;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Environment.
 */
public final class Environment {

    private final Map<String, IDecl> symbols;

    /**
     * Instantiates a new Environment.
     */
    public Environment() {
        symbols = new HashMap<>();
    }

    /**
     * Add.
     *
     * @param identifier the identifier
     * @param declaration the declaration
     */
    public void add(String identifier, IDecl declaration) {
        symbols.put(identifier, declaration);
    }

    /**
     * Get decl.
     *
     * @param identifier the identifier
     * @return the decl
     */
    public IDecl get(String identifier) {
        return symbols.get(identifier);
    }

}
