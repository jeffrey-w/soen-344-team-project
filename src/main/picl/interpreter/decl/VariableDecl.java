package main.picl.interpreter.decl;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The type Variable decl.
 */
public final class VariableDecl implements IDecl, Iterable<String> {

    private final Enum<?> type;
    private final Map<String, Integer> variables;

    /**
     * Instantiates a new Variable decl.
     *
     * @param type the type
     */
    public VariableDecl(IToken type) {
        this.type = type == null ? null : type.getType();
        this.variables = new HashMap<>();
    }

    /**
     * Add.
     *
     * @param identifier the identifier
     * @param value      the value
     */
    public void add(IToken identifier, IToken value) {
        variables.put((String) identifier.getValue(), value == null ? null : (Integer) value.getValue());
    }

    /**
     * Is const boolean.
     *
     * @return the boolean
     */
    public boolean isConst() {
        return type == null;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Enum<?> getType() {
        return type;
    }

    @Override
    public Iterator<String> iterator() {
        return variables.keySet().iterator();
    }

    /**
     * Get integer.
     *
     * @param identifier the identifier
     * @return the integer
     */
    public Integer get(String identifier) {
        return variables.get(identifier);
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return variables.size();
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitVariableDeclaration(this);
    }

}
