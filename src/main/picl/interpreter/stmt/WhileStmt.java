package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.picl.interpreter.expr.IExpr;

import java.util.*;

public final class WhileStmt implements IStmt, Iterable<Map.Entry<IExpr, IStmt>> {

    private final Map<IExpr, IStmt> guardedStatements;

    public WhileStmt() {
        guardedStatements = new LinkedHashMap<>();
    }

    public void addStatement(IExpr guard, IStmt statement) {
        guardedStatements.put(Objects.requireNonNull(guard), Objects.requireNonNull(statement));
    }

    @Override
    public void interpret(Environment environment) {
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitWhileStatement(this);
    }

    @Override
    public Iterator<Map.Entry<IExpr, IStmt>> iterator() {
        return guardedStatements.entrySet().iterator();
    }

}
