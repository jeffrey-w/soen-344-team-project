package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

/**
 * The type Binary expr.
 */
public final class BinaryExpr implements IExpr {

    private final IExpr left;
    private final IExpr right;
    private final Enum<?> operator;

    /**
     * Instantiates a new Binary expr.
     *
     * @param left the left
     * @param right the right
     * @param operator the operator
     */
    public BinaryExpr(IExpr left, IExpr right, IToken operator) {
        this.left = left;
        this.right = right;
        this.operator = operator.getType();
    }

    /**
     * Gets left.
     *
     * @return the left
     */
    public IExpr getLeft() {
        return left;
    }

    /**
     * Gets right.
     *
     * @return the right
     */
    public IExpr getRight() {
        return right;
    }

    /**
     * Gets operator.
     *
     * @return the operator
     */
    public Enum<?> getOperator() {
        return operator;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitBinaryExpression(this);
    }

}
