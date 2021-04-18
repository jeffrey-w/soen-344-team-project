package main.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Environment.
 */
public final class Environment {

    private final Map<String, IValue> symbols;

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
     * @param info the info
     */
    public void add(String identifier, IValue info) {
        symbols.put(identifier, info);
    }

    /**
     * Get decl.
     *
     * @param identifier the identifier
     * @return the decl
     */
    public IValue get(String identifier) {
        return symbols.get(identifier);
    }

}
