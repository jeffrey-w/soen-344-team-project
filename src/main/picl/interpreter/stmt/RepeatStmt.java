package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.expr.IExpr;

public final class RepeatStmt implements IStmt {

    private final IExpr condition;
    private final IStmt statements;

    public RepeatStmt(IExpr condition, IStmt statements) {
        this.condition = condition;
        this.statements = statements;
    }

    @Override
    public void interpret(Environment environment) {
        System.out.println("REPEAT");
        statements.interpret(environment); // TODO statements might be null
        if (condition != null) {
            System.out.print("UNTIL ");
            condition.interpret(environment);
        }
        System.out.print("END");
    }

}
