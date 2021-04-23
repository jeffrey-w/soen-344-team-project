package main.picl.interpreter.stmt;

import main.picl.interpreter.IVisitor;
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

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public IExpr getExpression() {
        return expression;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitExpressionStatement(this);
    }

    public boolean isDecrement() {
        return expression.isDecrement();
    }

}
