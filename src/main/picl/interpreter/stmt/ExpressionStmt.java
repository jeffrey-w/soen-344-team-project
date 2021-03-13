package main.picl.interpreter.stmt;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.picl.interpreter.expr.IExpr;

public final class ExpressionStmt implements IStmt {

    private final IExpr expression;

    public ExpressionStmt(IExpr expr) {
        this.expression = expr;
    }

    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitExpressionStatement(this);
    }

    public IExpr getExpression() {
        return expression;
    }
}
