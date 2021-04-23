package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import static main.picl.scanner.Token.TokenType.*;

/**
 * The type Logical expr.
 */
public class LogicalExpr extends BinaryExpr {

    /**
     * Instantiates a new Logical expr.
     *
     * @param left     the left
     * @param right    the right
     * @param operator the operator
     */
    public LogicalExpr(IExpr left, IExpr right, IToken operator) {
        super(left, right, validateOperator(operator, AND, OR));
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitLogicalExpression(this);
    }

    @Override
    public boolean isDecrement() {
        return false;
    }

}
