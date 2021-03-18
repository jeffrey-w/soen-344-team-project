package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;

/**
 * The type Get expr.
 */
public final class GetExpr implements IExpr {

    private final IExpr left;
    private final IExpr index;

    /**
     * Instantiates a new Get expr.
     *
     * @param left the left
     * @param index the index
     */
    public GetExpr(IExpr left, IExpr index) {
        this.left = left;
        this.index = index;
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
     * Gets index.
     *
     * @return the index
     */
    public IExpr getIndex() {
        return index;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitGetExpression(this);
    }

}
