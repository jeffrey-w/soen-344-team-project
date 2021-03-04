package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.expr.IExpr;

import java.util.List;

public final class IfStmt implements IStmt {

    private final IExpr thenCondition;
    private final IExpr elseCondition;
    private final IStmt thenStatements;
    private final IStmt elseStatement;
    private final List<IExpr> elses;
    private final List<IStmt> thens;

    public IfStmt(IExpr thenCondition, IStmt thenStatements, List<IExpr> elses, List<IStmt> thens,
            IExpr elseCondition, IStmt elseStatements) {
        this.thenCondition = thenCondition;
        this.thenStatements = thenStatements;
        this.elses = elses;
        this.thens = thens;
        this.elseCondition = elseCondition;
        this.elseStatement = elseStatements;
    }

    @Override
    public void interpret(Environment environment) {
        System.out.print("IF ");
        thenCondition.interpret(environment);
        System.out.print(" THEN\n");
        System.out.print("\t");
        thenStatements.interpret(environment);
        if (elses != null) {
            for (int i = 0; i < elses.size(); i++) {
                System.out.print("ELSIF ");
                elses.get(i).interpret(environment);
                System.out.print("THEN");
                thens.get(i).interpret(environment);
            }
        }
        if (elseCondition != null) {
            System.out.print("ELSE ");
            elseCondition.interpret(environment);
            elseStatement.interpret(environment);
        }
        System.out.print("\n\tEND\n");
    }
}
