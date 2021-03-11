package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
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

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitLiteralExpression(this);
    }

    public Integer getValue() {
        return value;
    }
}
