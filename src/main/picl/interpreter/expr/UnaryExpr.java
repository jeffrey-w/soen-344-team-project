package main.picl.interpreter.expr;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.scanner.IToken;

public final class UnaryExpr implements IExpr {

    private final Enum<?> operator;
    private final IExpr operand;

    public UnaryExpr(IToken operator, IExpr operand) {
        this.operand = operand;
        this.operator = operator.getType();
    }

    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitUnaryExpression(this);
    }

    public Enum<?> getOperator() {
        return operator;
    }

    public IExpr getOperand() {
        return operand;
    }

}
