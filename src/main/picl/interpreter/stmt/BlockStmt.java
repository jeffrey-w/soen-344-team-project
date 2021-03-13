package main.picl.interpreter.stmt;

import java.util.Iterator;
import java.util.List;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;

public final class BlockStmt implements IStmt, Iterable<IStmt> {

    private final List<IStmt> statements;

    public BlockStmt(List<IStmt> statements) {
        this.statements = statements;
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitBlockStatement(this);
    }

    @Override
    public Iterator<IStmt> iterator() {
        return statements.iterator();
    }

    public int size() {
        return statements.size();
    }
}
