package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;

/**
 * The type Call expr.
 */
public final class CallExpr implements IExpr {

    private final IExpr callee;
    private final IExpr argument;

    /**
     * Instantiates a new Call expr.
     *
     * @param callee   the callee
     * @param argument the argument
     */
    public CallExpr(IExpr callee, IExpr argument) {
        this.callee = callee;
        this.argument = argument;
    }

    /**
     * Gets callee.
     *
     * @return the callee
     */
    public IExpr getCallee() {
        return callee;
    }

    /**
     * Has argument boolean.
     *
     * @return the boolean
     */
    public boolean hasArgument() {
        return argument != null;
    }

    /**
     * Gets argument.
     *
     * @return the argument
     */
    public IExpr getArgument() {
        return argument;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitCallExpression(this);
    }

    @Override
    public boolean isDecrement() {
        return false;
    }

}
