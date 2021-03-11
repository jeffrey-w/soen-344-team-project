package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

public final class BinaryExpr implements IExpr {

    private final IExpr left;
    private final IExpr right;
    private final Enum<?> operator;

    public BinaryExpr(IExpr left, IExpr right, IToken operator) {
        this.left = left;
        this.right = right;
        this.operator = operator.getType();
    }

    @Override
    public void interpret(Environment environment) {
    }

    @Override
    public void accept(final IVisitor visitor) {
        visitor.visitBinaryExpression(this);
    }

    public IExpr getLeft() {
        return left;
    }

    public IExpr getRight() {
        return right;
    }

    public Enum<?> getOperator() {
        return operator;
    }

}
