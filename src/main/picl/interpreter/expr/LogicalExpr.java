package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import static main.picl.scanner.Token.TokenType.*;

public class LogicalExpr extends BinaryExpr {

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
