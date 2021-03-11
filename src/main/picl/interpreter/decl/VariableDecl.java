package main.picl.interpreter.decl;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    public void interpret(Environment environment) {
        StringBuilder builder = new StringBuilder();
        builder.append(isConst() ? "CONST" : type).append(" ");
        int index = 0;
        for (String identifier : variables.keySet()) {
            builder.append(identifier);
            Integer value = variables.get(identifier);
            if (value != null) {
                builder.append(" = ").append(value);
            }
            if (++index < variables.size()) {
                builder.append(", ");
            } else {
                builder.append(";");
            }
        }
        System.out.println(builder);
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitVariableDeclaration(this);
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
