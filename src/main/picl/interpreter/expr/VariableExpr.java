package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

public final class VariableExpr implements IExpr {

    private final IToken identifier;

    public VariableExpr(IToken identifier) {
        this.identifier = identifier;
    }

    @Override
    public void interpret(Environment environment) {
        System.out.print(identifier.getValue());
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitVariableExpression(this);
    }

    public IToken getIdentifier() {
        return identifier;
    }
}
