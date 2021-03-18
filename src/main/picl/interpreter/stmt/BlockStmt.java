package main.picl.interpreter.stmt;

import main.picl.interpreter.IVisitor;

import java.util.Iterator;
import java.util.List;

/**
 * The type Block stmt.
 */
public final class BlockStmt implements IStmt, Iterable<IStmt> {

    private final List<IStmt> statements;

    /**
     * Instantiates a new Block stmt.
     *
     * @param statements the statements
     */
    public BlockStmt(List<IStmt> statements) {
        this.statements = statements;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return statements.size();
    }

    @Override
    public Iterator<IStmt> iterator() {
        return statements.iterator();
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitBlockStatement(this);
    }

}
