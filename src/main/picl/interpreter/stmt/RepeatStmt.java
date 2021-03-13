package main.picl.interpreter.stmt;

import main.parser.IVisitor;
import main.picl.interpreter.expr.IExpr;
import main.picl.interpreter.IPICLVisitor;

public final class RepeatStmt implements IStmt {

    private final IExpr guard;
    private final IStmt statements;

    public RepeatStmt(IExpr guard, IStmt statements) {
        this.guard = guard;
        this.statements = statements;
    }


    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitRepeatStatement(this);
    }

    public IStmt getStatements() {
        return statements;
    }

    public boolean hasGuard() {
        return guard != null;
    }

    public IExpr getGuard() {
        return guard;
    }
}
