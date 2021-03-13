package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;


public final class GetExpr implements IExpr {

    private final IExpr left;
    private final IExpr index;

    public GetExpr(IExpr left, IExpr index) {
        this.left = left;
        this.index = index;
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitGetExpression(this);
    }

    public IExpr getLeft() {
        return left;
    }

    public IExpr getIndex() {
        return index;
    }
}
