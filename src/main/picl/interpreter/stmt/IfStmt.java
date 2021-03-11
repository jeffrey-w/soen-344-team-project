package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.picl.interpreter.expr.IExpr;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class IfStmt implements IStmt, Iterable<Map.Entry<IExpr, IStmt>> {

    private final Map<IExpr, IStmt> guardedStatements;

    public IfStmt() {
        this.guardedStatements = new LinkedHashMap<>();
    }

    public void addStatement(IExpr guard, IStmt statement) {
        guardedStatements.put(Objects.requireNonNull(guard), Objects.requireNonNull(statement));
    }

    public void addElse(IStmt statement) {
        guardedStatements.put(null, Objects.requireNonNull(statement));
    }

    @Override
    public void interpret(Environment environment) {

    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitIfStatement(this);
    }

    @Override
    public Iterator<Map.Entry<IExpr, IStmt>> iterator() {
        return guardedStatements.entrySet().iterator();
    }

}
