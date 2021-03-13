package main.picl.interpreter.stmt;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.picl.interpreter.expr.IExpr;

import java.util.Objects;

public class ReturnStmt implements IStmt {

    private final IExpr expression;

    public ReturnStmt(IExpr expression) {
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitReturnStatement(this);
    }

    public IExpr getExpression() {
        return expression;
    }

}
