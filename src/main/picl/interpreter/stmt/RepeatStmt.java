package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.picl.interpreter.expr.IExpr;

public final class RepeatStmt implements IStmt {

    private final IExpr guard;
    private final IStmt statements;

    public RepeatStmt(IExpr guard, IStmt statements) {
        this.guard = guard;
        this.statements = statements;
    }

    @Override
    public void interpret(Environment environment) {
        System.out.println("REPEAT");
        statements.interpret(environment); // TODO statements might be null
        if (guard != null) {
            System.out.print("UNTIL ");
            guard.interpret(environment);
        }
        System.out.print("END");
    }

    @Override
    public void accept(final IVisitor visitor) {
        visitor.visitRepeatStatement(this);
    }

    public IStmt getStatements() {
        return statements;
    }

    public boolean hasGuard() {
        return guard != null;
    }

    public IExpr getGuard() {
        return guard;
    }
}
