package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import static main.picl.scanner.Token.TokenType.*;

/**
 * The type Arithmetic expr.
 */
public class ArithmeticExpr extends BinaryExpr {

    /**
     * Instantiates a new Arithmetic expr.
     *
     * @param left     the left
     * @param right    the right
     * @param operator the operator
     */
    public ArithmeticExpr(IExpr left, IExpr right, IToken operator) {
        super(left, right, validateOperator(operator, PLUS, MINUS, AST, SLASH));
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitArithmeticExpression(this);
    }

    @Override
    public boolean isDecrement() {
        return false;
    }

}