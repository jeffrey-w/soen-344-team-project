package main.picl.interpreter;

import main.parser.ISyntaxTree;
import main.picl.interpreter.decl.*;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;
import main.picl.scanner.Token;

import java.util.EnumMap;
import java.util.Map;

/**
 * The type Pretty printer.
 */
public class PrettyPrinter implements IVisitor {

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

    private int scopeDepth;
    private final ISyntaxTree<INode> syntaxTree;

    /**
     * Instantiates a new Pretty printer.
     *
     * @param syntaxTree the syntax tree
     */
    public PrettyPrinter(ISyntaxTree<INode> syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    /**
     * Print.
     */
    public void print() {
        syntaxTree.getHead().accept(this);
    }

    @Override
    public void visitModuleDeclaration(ModuleDecl declaration) {
        print("MODULE " + declaration.getIdentifier() + ";\n");
        for (IDecl decl : declaration.getDeclarations()) {
            enterScope(decl);
        }
        if (declaration.hasStatements()) {
            print("BEGIN\n");
            enterScope(declaration.getStatements());
        }
        System.out.println("END " + declaration.getIdentifier() + ".");
    }

    @Override
    public void visitVariableDeclaration(VariableDecl declaration) {
        StringBuilder builder = new StringBuilder();
        builder.append(declaration.isConst() ? "CONST" : declaration.getType()).append(" ");
        int index = 0;
        for (String identifier : declaration) {
            builder.append(identifier);
            Integer value = declaration.get(identifier);
            if (value != null) {
                builder.append(" = ").append(value);
            }
            if (++index < declaration.size()) {
                builder.append(", ");
            } else {
                builder.append(";");
            }
        }
        indent();
        System.out.println(builder);
    }

    @Override
    public void visitProcedureDeclaration(ProcedureDecl declaration) {
        print("PROCED " + declaration.getIdentifier());
        if (declaration.hasParameter()) {
            declaration.getParameter().accept(this);
        }
        if (declaration.hasReturnType()) {
            System.out.print(": " + declaration.getReturnType());
        }
        System.out.println(";");
        for (IDecl decl : declaration.getDeclarations()) {
            enterScope(decl);
        }
        if (declaration.hasStatements()) {
            print("BEGIN\n");
            enterScope(declaration.getStatements());
        }
        if (declaration.hasReturnStatement()) {
            enterScope(declaration.getReturnStatement());
        }
        print("END " + declaration.getIdentifier() + ";\n");
    }

    @Override
    public void visitParameterDeclaration(ParameterDecl declaration) {
        System.out.print("(" + declaration.getType() + " " + declaration.getIdentifier() + ")");
    }

    @Override
    public void visitBlockStatement(BlockStmt statement) {
        for (IStmt stmt : statement) {
            stmt.accept(this);
            if (statement.size() > 1 && isExpr(stmt)) {
                System.out.println(";");
            } else {
                System.out.println();
            }
        }
    }

    private boolean isExpr(IStmt statement) {
        return statement instanceof ExpressionStmt;
    }

    @Override
    public void visitIfStatement(IfStmt statement) {
        int count = 0;
        for (Map.Entry<IExpr, IStmt> guardedStmt : statement) {
            if (guardedStmt.getKey() == null) {
                print("ELSE\n");
            } else {
                if (++count > 1) {
                    print("ELSIF ");
                } else {
                    print("IF ");
                }
                guardedStmt.getKey().accept(this);
                System.out.println(" THEN");
            }
            enterScope(guardedStmt.getValue());
        }
        print("END");
    }

    @Override
    public void visitWhileStatement(WhileStmt statement) {
        int count = 0;
        for (Map.Entry<IExpr, IStmt> guardedStmt : statement) {
            if (++count > 1) {
                print("ELSIF ");
            } else {
                print("WHILE ");
            }
            guardedStmt.getKey().accept(this);
            System.out.println(" DO");
            enterScope(guardedStmt.getValue());
        }
        print("END");
    }

    @Override
    public void visitRepeatStatement(RepeatStmt statement) {
        print("REPEAT\n");
        enterScope(statement.getStatements());
        if (statement.hasGuard()) {
            print("UNTIL ");
            statement.getGuard().accept(this);
            System.out.print(";");
        } else {
            print("END");
        }
    }

    @Override
    public void visitReturnStatement(ReturnStmt statement) {
        print("RETURN ");
        statement.getExpression().accept(this);
        System.out.println();
    }

    @Override
    public void visitExpressionStatement(ExpressionStmt statement) {
        print("");
        statement.getExpression().accept(this);
    }

    @Override
    public void visitAssignmentExpression(AssignmentExpr expression) {
        visitBinaryExpression(expression);
    }

    @Override
    public void visitLogicalExpression(LogicalExpr expression) {
        visitBinaryExpression(expression);
    }

    @Override
    public void visitComparisonExpression(ComparisonExpr expression) {
        visitBinaryExpression(expression);
    }

    @Override
    public void visitArithmenticExpression(ArithmeticExpr expression) {
        visitBinaryExpression(expression);
    }

    private void visitBinaryExpression(BinaryExpr expression) {
        expression.getLeft().accept(this);
        System.out.print(SYMBOLS.get(expression.getOperator()));
        expression.getRight().accept(this);
    }

    @Override
    public void visitUnaryExpression(UnaryExpr expression) {
        Enum<?> operator = expression.getOperator();
        if (operator == Token.TokenType.QUERY) {
            System.out.print("?");
        } else if (operator == Token.TokenType.OP) {
            System.out.print("!");
        } else if (operator == Token.TokenType.NOT) {
            System.out.print("~");
        } else {
            System.out.print(operator + " ");
        }
        expression.getOperand().accept(this);
    }

    @Override
    public void visitCallExpression(CallExpr expression) {
        expression.getCallee().accept(this);
        if (expression.hasArgument()) {
            System.out.print("(");
            expression.getArgument().accept(this);
            System.out.print(")");
        }
    }

    @Override
    public void visitGetExpression(GetExpr expression) {
        expression.getLeft().accept(this);
        System.out.print(".");
        expression.getIndex().accept(this);
    }

    @Override
    public void visitVariableExpression(VariableExpr expression) {
        System.out.print(expression.getIdentifier().getValue());
    }

    @Override
    public void visitLiteralExpression(LiteralExpr expression) {
        System.out.print(expression.getValue());
    }

    private void enterScope(INode node) {
        scopeDepth++;
        node.accept(this);
        scopeDepth--;
    }

    private void print(String line) {
        indent();
        System.out.print(line);
    }

    private void indent() {
        for (int i = 0; i < scopeDepth; i++) {
            System.out.print("\t");
        }
    }

}
