package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import static main.picl.scanner.Token.TokenType.*;

/**
 * The type Assignment expr.
 */
public class AssignmentExpr extends BinaryExpr {

    /**
     * Instantiates a new Assignment expr.
     *
     * @param left     the left
     * @param right    the right
     * @param operator the operator
     */
    public AssignmentExpr(IExpr left, IExpr right, IToken operator) {
        super(left, right, validateOperator(operator, BECOMES));
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitAssignmentExpression(this);        
    }

    @Override
    public boolean isDecrement() {
        return false;
    }

}
