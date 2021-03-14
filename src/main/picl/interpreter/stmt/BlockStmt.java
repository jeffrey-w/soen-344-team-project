package main.picl.interpreter.stmt;

import java.util.Iterator;
import java.util.List;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;

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

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitBlockStatement(this);
    }

    @Override
    public Iterator<IStmt> iterator() {
        return statements.iterator();
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return statements.size();
    }
}
