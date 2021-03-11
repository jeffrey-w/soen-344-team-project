package main.picl.interpreter.stmt;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;

import java.util.Iterator;
import java.util.List;

public final class BlockStmt implements IStmt, Iterable<IStmt> {

    private final List<IStmt> statements;

    public BlockStmt(List<IStmt> statements) {
        this.statements = statements;
    }

    @Override
    public void interpret(Environment environment) {
        for (IStmt statement : statements) {
            System.out.print("\t");
            statement.interpret(environment);
            if (statements.size() > 1 && isExpr(statement)) {
                System.out.print(";\n");
            }
        }
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitBlockStatement(this);
    }

    private boolean isExpr(IStmt statement) {
        return statement instanceof ExpressionStmt;
    }

    @Override
    public Iterator<IStmt> iterator() {
        return statements.iterator();
    }

    public int size() {
        return statements.size();
    }
}
