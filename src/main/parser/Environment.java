package main.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Environment.
 */
public final class Environment {

    /**
     * The type Entry info.
     */
    public static class EntryInfo {
        /**
         * The Type.
         */
        public Enum<?> type;
        /**
         * The Value.
         */
        public int value;

        /**
         * Instantiates a new Entry info.
         *
         * @param type  the type
         * @param value the value
         */
        public EntryInfo(Enum<?> type, int value) {
            this.type = type;
            this.value = value;
        }

    }

    private final Map<String, EntryInfo> symbols;

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
     * @param info       the info
     */
    public void add(String identifier, EntryInfo info) {
        symbols.put(identifier, info);
    }

    /**
     * Get decl.
     *
     * @param identifier the identifier
     * @return the decl
     */
    public EntryInfo get(String identifier) {
        return symbols.get(identifier);
    }

}
