package main.picl.interpreter.decl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.scanner.IToken;

public final class VariableDecl implements IDecl, Iterable<String> {

    private final Enum<?> type;
    private final Map<String, Integer> variables;

    public VariableDecl(IToken type) {
        this.type = type == null ? null : type.getType();
        this.variables = new HashMap<>();
    }

    public void add(IToken identifier, IToken value) {
        variables.put((String) identifier.getValue(), value == null ? null : (Integer) value.getValue());
    }

    public boolean isConst() {
        return type == null;
    }


    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitVariableDeclaration(this);
    }

    public Enum<?> getType() {
        return type;
    }

    @Override
    public Iterator<String> iterator() {
        return variables.keySet().iterator();
    }

    public Integer get(String identifier) {
        return variables.get(identifier);
    }

    public int size() {
        return variables.size();
    }
}
