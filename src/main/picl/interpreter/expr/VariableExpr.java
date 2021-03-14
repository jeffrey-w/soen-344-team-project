package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.scanner.IToken;
import main.picl.interpreter.IPICLVisitor;

/**
 * The type Variable expr.
 */
public final class VariableExpr implements IExpr {

    private final IToken identifier;

    /**
     * Instantiates a new Variable expr.
     *
     * @param identifier the identifier
     */
    public VariableExpr(IToken identifier) {
        this.identifier = identifier;
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitVariableExpression(this);
    }

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public IToken getIdentifier() {
        return identifier;
    }
}
