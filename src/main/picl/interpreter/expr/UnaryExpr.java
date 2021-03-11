package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.picl.scanner.Token;
import main.scanner.IToken;

public final class UnaryExpr implements IExpr {

    private final Enum<?> operator;
    private final IExpr operand;

    public UnaryExpr(IToken operator, IExpr operand) {
        this.operand = operand;
        this.operator = operator.getType();
    }

    @Override
    public void interpret(Environment environment) {
        if (operator.equals(Token.TokenType.OP)) {
            System.out.print("!");
        } else if (operator.equals(Token.TokenType.NOT)) {
            System.out.print("~");
        } else {
            System.out.print(operator + " ");
        }
        operand.interpret(environment);
    }

    @Override
    public void accept(final IVisitor visitor) {
        visitor.visitUnaryExpression(this);
    }

    public Enum<?> getOperator() {
        return operator;
    }

    public IExpr getOperand() {
        return operand;
    }

}
