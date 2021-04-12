package main.picl.interpreter.expr;

import main.scanner.IToken;

/**
 * The type Binary expr.
 */
public abstract class BinaryExpr implements IExpr {

    private final IExpr left;
    private final IExpr right;
    private final Enum<?> operator;

    static IToken validateOperator(IToken operator, Enum<?>... validTypes) {
        for (Enum<?> type : validTypes) {
            if (operator.getType() == type) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Invalid operator: " + operator.getType() + ".");
    }


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

}
