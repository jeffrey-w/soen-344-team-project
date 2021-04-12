package main.picl.interpreter.expr;

import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

import static main.picl.scanner.Token.TokenType.*;

public class ArithmeticExpr extends BinaryExpr {

    public ArithmeticExpr(IExpr left, IExpr right, IToken operator) {
        super(left, right, validateOperator(operator, PLUS, MINUS, AST, SLASH));
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitArithmeticExpression(this);
    }

}