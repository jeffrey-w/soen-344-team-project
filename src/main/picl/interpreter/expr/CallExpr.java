package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;


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

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitCallExpression(this);
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

}
