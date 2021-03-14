package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
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
     * @param left     the left
     * @param right    the right
     * @param operator the operator
     */
    public BinaryExpr(IExpr left, IExpr right, IToken operator) {
        this.left = left;
        this.right = right;
        this.operator = operator.getType();
    }



    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitBinaryExpression(this);
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

}
