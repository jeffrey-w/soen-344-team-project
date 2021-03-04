package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;
import main.scanner.IToken;

public final class VariableExpr implements IExpr {

    private final IToken identifier;

    public VariableExpr(IToken identifier) {
        this.identifier = identifier;
    }

    @Override
    public void interpret(final Environment environment) {
        System.out.print(identifier.getValue());
    }

}
