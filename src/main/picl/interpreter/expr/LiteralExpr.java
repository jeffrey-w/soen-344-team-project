package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
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

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitLiteralExpression(this);
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Integer getValue() {
        return value;
    }
}
