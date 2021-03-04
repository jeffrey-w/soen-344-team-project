package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;

public final class GetExpr implements IExpr {

    private final IExpr left;
    private final IExpr index;

    public GetExpr(IExpr left, IExpr index) {
        this.left = left;
        this.index = index;
    }

    @Override
    public void interpret(Environment environment) {
        left.interpret(environment);
        System.out.print(".");
        index.interpret(environment);
    }

}
