package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.picl.scanner.Token;
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

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitUnaryExpression(this);
    }

    @Override
    public boolean isDecrement() {
        return operator == Token.TokenType.DEC;
    }

    public boolean hasGet() {
        return operand instanceof GetExpr;
    }

    public boolean hasUnaryNot() {
        return operand instanceof UnaryExpr && ((UnaryExpr) operand).getOperator() == Token.TokenType.NOT;
    }

}
