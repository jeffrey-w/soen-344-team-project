package main.picl.interpreter.expr;

import main.picl.interpreter.Environment;
import main.picl.scanner.Token;
import main.scanner.IToken;

import java.util.EnumMap;
import java.util.Map;

public final class BinaryExpr implements IExpr {

    private static final Map<Token.TokenType, String> SYMBOLS = new EnumMap<>(Token.TokenType.class);

    static {
        SYMBOLS.put(Token.TokenType.BECOMES, " := ");
        SYMBOLS.put(Token.TokenType.OR, " OR ");
        SYMBOLS.put(Token.TokenType.AND, " & ");
        SYMBOLS.put(Token.TokenType.EQL, " = ");
        SYMBOLS.put(Token.TokenType.NEQ, " # ");
        SYMBOLS.put(Token.TokenType.GTR, " > ");
        SYMBOLS.put(Token.TokenType.GEQ, " >= ");
        SYMBOLS.put(Token.TokenType.LSS, " < ");
        SYMBOLS.put(Token.TokenType.LEQ, " <= ");
        SYMBOLS.put(Token.TokenType.PLUS, " + ");
        SYMBOLS.put(Token.TokenType.MINUS, " - ");
        SYMBOLS.put(Token.TokenType.AST, " * ");
        SYMBOLS.put(Token.TokenType.SLASH, " / ");
    }

    private final IExpr left;
    private final IExpr right;
    private final Enum<?> operator;

    public BinaryExpr(IExpr left, IExpr right, IToken operator) {
        this.left = left;
        this.right = right;
        this.operator = operator.getType();
    }

    @Override
    public void interpret(Environment environment) {
        left.interpret(environment);
        System.out.print(SYMBOLS.get(operator)); // TODO suspicious call
        right.interpret(environment);
    }
}
