package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;

public final class CallExpr implements IExpr {

    private final IExpr callee;
    private final IExpr argument;

    public CallExpr(IExpr callee, IExpr argument) {
        this.callee = callee;
        this.argument = argument;
    }

    @Override
    public void interpret(Environment environment) {
        callee.interpret(environment);
        if (argument != null) {
            System.out.print("(");
            argument.interpret(environment);
            System.out.print(")");
        }
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitCallExpression(this);
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
