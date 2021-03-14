package main.picl.interpreter.stmt;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.picl.interpreter.expr.IExpr;

/**
 * The type Expression stmt.
 */
public final class ExpressionStmt implements IStmt {

    private final IExpr expression;

    /**
     * Instantiates a new Expression stmt.
     *
     * @param expr the expr
     */
    public ExpressionStmt(IExpr expr) {
        this.expression = expr;
    }

    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitExpressionStatement(this);
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public IExpr getExpression() {
        return expression;
    }
}
