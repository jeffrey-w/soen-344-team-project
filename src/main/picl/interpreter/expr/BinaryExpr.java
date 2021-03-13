package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
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
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitBinaryExpression(this);
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
