package main.picl.interpreter.stmt;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.picl.interpreter.expr.IExpr;

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
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitIfStatement(this);
    }

    @Override
    public Iterator<Map.Entry<IExpr, IStmt>> iterator() {
        return guardedStatements.entrySet().iterator();
    }

}
