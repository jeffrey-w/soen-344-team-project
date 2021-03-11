package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.picl.interpreter.expr.IExpr;

import java.util.Objects;

public class ReturnStmt implements IStmt {

    private final IExpr expression;

    public ReturnStmt(IExpr expression) {
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public void interpret(Environment environment) {

    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitReturnStatement(this);
    }

    public IExpr getExpression() {
        return expression;
    }

}
