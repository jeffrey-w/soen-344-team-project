package main.picl.interpreter.stmt;

import main.picl.interpreter.IVisitor;
import main.picl.interpreter.expr.ComparisonExpr;
import main.picl.interpreter.expr.IExpr;
import main.picl.interpreter.expr.LiteralExpr;
import main.picl.scanner.Token;

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

    public IExpr isSpecial(boolean isDecrement) {
        if (isDecrement) {
            if (hasGuard() && guard instanceof ComparisonExpr) {
                ComparisonExpr comparison = (ComparisonExpr) guard;
                if (comparison.getOperator() == Token.TokenType.EQL) {
                    IExpr left = comparison.getLeft(), right = comparison.getRight();
                    if (left instanceof LiteralExpr && ((LiteralExpr) left).getValue() == 0) {
                        return right;
                    }
                    if (right instanceof LiteralExpr && ((LiteralExpr) right).getValue() == 0) {
                        return left;
                    }
                }
            }
        }
        return null;
    }

}
