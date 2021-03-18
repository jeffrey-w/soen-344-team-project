package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

/**
 * The type Literal expr.
 */
public final class LiteralExpr implements IExpr {

    private final Integer value;

    /**
     * Instantiates a new Literal expr.
     *
     * @param value the value
     */
    public LiteralExpr(IToken value) {
        this.value = (Integer) value.getValue();
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Integer getValue() {
        return value;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitLiteralExpression(this);
    }

}
