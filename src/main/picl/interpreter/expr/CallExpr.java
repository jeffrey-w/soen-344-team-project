package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;


public final class CallExpr implements IExpr {

    private final IExpr callee;
    private final IExpr argument;

    public CallExpr(IExpr callee, IExpr argument) {
        this.callee = callee;
        this.argument = argument;
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitCallExpression(this);
    }

    public IExpr getCallee() {
        return callee;
    }

    public boolean hasArgument() {
        return argument != null;
    }

    public IExpr getArgument() {
        return argument;
    }

}
