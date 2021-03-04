package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.expr.IExpr;

import java.util.List;

public final class WhileStmt implements IStmt {

    private final IExpr condition;
    private final IStmt statements;
    private final List<IExpr> elses;
    private final List<IStmt> thens;

    public WhileStmt(IExpr condition, IStmt statements, List<IExpr> elses, List<IStmt> thens) {
        this.condition = condition;
        this.statements = statements;
        this.elses = elses;
        this.thens = thens;
    }

    @Override
    public void interpret(final Environment environment) {
        System.out.print("WHILE ");
        condition.interpret(environment);
        System.out.println("DO");
        statements.interpret(environment);
        for (int i = 0; i < elses.size(); i++) {
            System.out.print("ELSIF ");
            elses.get(i).interpret(environment);
            System.out.println("DO");
            System.out.print("\t");
            thens.get(i).interpret(environment);
            System.out.println();
        }
        System.out.println("END");
    }

}
