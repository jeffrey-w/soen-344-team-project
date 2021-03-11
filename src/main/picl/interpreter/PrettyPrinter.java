package main.picl.interpreter;

import main.picl.interpreter.decl.*;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;
import main.picl.parser.Parser;
import main.picl.scanner.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

public class PrettyPrinter implements IVisitor {

    public static void main(String[] args) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("./programs/Example.mod"));
            Parser parser = new Parser(new String(bytes));
            IDecl module = parser.parse();
            PrettyPrinter printer = new PrettyPrinter();
            printer.visitModuleDeclaration((ModuleDecl) module);
        } catch (IOException e) {
            System.err.println("Unable to open file.");
            System.exit(0x10); // TODO
        }
    }

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
    private SyntaxTree ast;

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
            System.out.println(": " + declaration.getReturnType());
        }
        System.out.println();
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
        print("END " + declaration.getIdentifier() + ".\n");
    }

    @Override
    public void visitParameterDeclaration(ParameterDecl declaration) {
        System.out.println("(" + declaration.getType() + " " + declaration.getIdentifier() + ")");
    }

    @Override
    public void visitBlockStatement(BlockStmt statement) {
        for (IStmt stmt : statement) {
            stmt.accept(this);
            if (statement.size() > 1 && isExpr(stmt)) {
                System.out.println(";");
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
                print("ELSE ");
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
        System.out.println();
        print("END\n");
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
        System.out.println();
        print("END\n");
    }

    @Override
    public void visitRepeatStatement(RepeatStmt statement) {
        System.out.println("REPEAT");
        enterScope(statement.getStatements());
        if (statement.hasGuard()) {
            System.out.print("UNTIL ");
            statement.getGuard().accept(this);
        }
        System.out.println();
        print("END\n");
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
    public void visitBinaryExpression(BinaryExpr expression) {
        expression.getLeft().accept(this);
        System.out.print(SYMBOLS.get(expression.getOperator())); // TODO
        expression.getRight().accept(this);
    }

    @Override
    public void visitUnaryExpression(UnaryExpr expression) {
        Enum<?> operator = expression.getOperator();
        if (operator == Token.TokenType.OP) {
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
