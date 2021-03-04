package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.expr.IExpr;

public class ReturnStmt implements IStmt {

    private final IExpr expression;

    public ReturnStmt(IExpr expression) {
        this.expression = expression;
    }

    @Override
    public void interpret(Environment environment) {
        System.out.print("RETURN ");
        expression.interpret(environment);
    }

}
