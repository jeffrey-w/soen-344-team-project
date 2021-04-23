package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

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

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public IToken getIdentifier() {
        return identifier;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitVariableExpression(this);
    }

    @Override
    public boolean isDecrement() {
        return false;
    }

}
