package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.scanner.IToken;

/**
 * The type Unary expr.
 */
public final class UnaryExpr implements IExpr {

    private final Enum<?> operator;
    private final IExpr operand;

    /**
     * Instantiates a new Unary expr.
     *
     * @param operator the operator
     * @param operand  the operand
     */
    public UnaryExpr(IToken operator, IExpr operand) {
        this.operand = operand;
        this.operator = operator.getType();
    }

    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitUnaryExpression(this);
    }

    /**
     * Gets operator.
     *
     * @return the operator
     */
    public Enum<?> getOperator() {
        return operator;
    }

    /**
     * Gets operand.
     *
     * @return the operand
     */
    public IExpr getOperand() {
        return operand;
    }

}
