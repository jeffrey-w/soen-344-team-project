package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.scanner.IToken;
import main.picl.interpreter.IPICLVisitor;

public final class VariableExpr implements IExpr {

    private final IToken identifier;

    public VariableExpr(IToken identifier) {
        this.identifier = identifier;
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitVariableExpression(this);
    }

    public IToken getIdentifier() {
        return identifier;
    }
}
