package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;
import main.scanner.IToken;

public final class LiteralExpr implements IExpr {

    private final Integer value;

    public LiteralExpr(IToken value) {
        this.value = (Integer) value.getValue();
    }

    @Override
    public void interpret(Environment environment) {
        System.out.print(value);
    }
}
