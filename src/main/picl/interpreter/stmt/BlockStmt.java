package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;

import java.util.List;

public final class BlockStmt implements IStmt {

    private final List<IStmt> statements;

    public BlockStmt(List<IStmt> statements) {
        this.statements = statements;
    }

    @Override
    public void interpret(final Environment environment) {
        for (IStmt statement : statements) {
            System.out.print("\t");
            statement.interpret(environment);
            if (statements.size() > 1 && isExpr(statement)) {
                System.out.print(";\n");
            }
        }
    }

    private boolean isExpr(IStmt statement) {
        return statement instanceof ExpressionStmt;
    }

}
