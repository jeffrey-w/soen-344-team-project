package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.scanner.IToken;


public final class LiteralExpr implements IExpr {

    private final Integer value;

    public LiteralExpr(IToken value) {
        this.value = (Integer) value.getValue();
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitLiteralExpression(this);
    }

    public Integer getValue() {
        return value;
    }
}
