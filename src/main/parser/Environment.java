package main.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Environment.
 */
public final class Environment {

    private final Environment parent;
    private final Map<String, IValue> symbols;

    /**
     * Instantiates a new Environment.
     */
    public Environment() {
        this(null);
    }

    public Environment(Environment parent) {
        this.parent = parent;
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
        Environment currentScope = this;
        IValue value;
        do {
            value = currentScope.symbols.get(identifier);
            currentScope = currentScope.parent;
        } while (currentScope != null && value == null);
        return value; // TODO throw picl error for undefined variable
    }

    public Environment getParent() {
        return parent;
    }
}
