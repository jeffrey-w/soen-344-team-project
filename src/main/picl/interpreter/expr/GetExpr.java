package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;


/**
 * The type Get expr.
 */
public final class GetExpr implements IExpr {

    private final IExpr left;
    private final IExpr index;

    /**
     * Instantiates a new Get expr.
     *
     * @param left  the left
     * @param index the index
     */
    public GetExpr(IExpr left, IExpr index) {
        this.left = left;
        this.index = index;
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitGetExpression(this);
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
}
