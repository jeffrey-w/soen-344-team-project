package main.picl.interpreter.stmt;

import main.picl.interpreter.IVisitor;
import main.picl.interpreter.expr.IExpr;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The type While stmt.
 */
public final class WhileStmt implements IStmt, Iterable<Map.Entry<IExpr, IStmt>> {

    private final Map<IExpr, IStmt> guardedStatements;

    /**
     * Instantiates a new While stmt.
     */
    public WhileStmt() {
        guardedStatements = new LinkedHashMap<>();
    }

    /**
     * Add statement.
     *
     * @param guard     the guard
     * @param statement the statement
     */
    public void addStatement(IExpr guard, IStmt statement) {
        guardedStatements.put(Objects.requireNonNull(guard), Objects.requireNonNull(statement));
    }

    @Override
    public Iterator<Map.Entry<IExpr, IStmt>> iterator() {
        return guardedStatements.entrySet().iterator();
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitWhileStatement(this);
    }

}
