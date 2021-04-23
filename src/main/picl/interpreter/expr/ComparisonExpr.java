package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import static main.picl.scanner.Token.TokenType.*;

/**
 * The type Comparison expr.
 */
public class ComparisonExpr extends BinaryExpr {

    /**
     * Instantiates a new Comparison expr.
     *
     * @param left     the left
     * @param right    the right
     * @param operator the operator
     */
    public ComparisonExpr(IExpr left, IExpr right, IToken operator) {
        super(left, right, validateOperator(operator, EQL, NEQ, GTR, GEQ, LSS, LEQ));
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitComparisonExpression(this);        
    }

    @Override
    public boolean isDecrement() {
        return false;
    }

}
