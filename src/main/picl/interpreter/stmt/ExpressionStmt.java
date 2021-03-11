package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.picl.interpreter.expr.IExpr;

public final class ExpressionStmt implements IStmt {

    private final IExpr expression;

    public ExpressionStmt(IExpr expr) {
        this.expression = expr;
    }

    @Override
    public void interpret(final Environment environment) {
        expression.interpret(environment);
    }

    @Override
    public void accept(final IVisitor visitor) {
        visitor.visitExpressionStatement(this);
    }

    public IExpr getExpression() {
        return expression;
    }
}
