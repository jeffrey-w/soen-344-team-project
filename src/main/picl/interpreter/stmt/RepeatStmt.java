package main.picl.interpreter.stmt;

import main.picl.interpreter.IVisitor;
import main.picl.interpreter.expr.IExpr;

/**
 * The type Repeat stmt.
 */
public final class RepeatStmt implements IStmt {

    private final IExpr guard;
    private final IStmt statements;

    /**
     * Instantiates a new Repeat stmt.
     *
     * @param guard      the guard
     * @param statements the statements
     */
    public RepeatStmt(IExpr guard, IStmt statements) {
        this.guard = guard;
        this.statements = statements;
    }

    /**
     * Gets statements.
     *
     * @return the statements
     */
    public IStmt getStatements() {
        return statements;
    }

    /**
     * Has guard boolean.
     *
     * @return the boolean
     */
    public boolean hasGuard() {
        return guard != null;
    }

    /**
     * Gets guard.
     *
     * @return the guard
     */
    public IExpr getGuard() {
        return guard;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitRepeatStatement(this);
    }

}
