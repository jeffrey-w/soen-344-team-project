package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import static main.picl.scanner.Token.TokenType.*;

public class ComparisonExpr extends BinaryExpr {

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
