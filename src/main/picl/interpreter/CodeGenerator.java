package main.picl.interpreter;

import main.parser.Environment;
import main.parser.IParser;
import main.parser.IValue;
import main.picl.interpreter.decl.*;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;
import main.picl.parser.LiteralValue;
import main.picl.parser.MemoryAddressValue;
import main.picl.parser.Parser;
import main.picl.parser.SyntaxTree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class CodeGenerator implements IVisitor {

    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("./programs/Assignments.mod"));
        IParser<INode> parser = new Parser(new String(bytes));
        CodeGenerator generator = new CodeGenerator((SyntaxTree) parser.parse());
        generator.generate();
    }

    private int line;
    private int address; // TODO move to Environment
    private final SyntaxTree ast;
    private final Environment globals;
    private IValue stackTop;
    private final StringBuilder output; // TODO rename this

    public CodeGenerator(SyntaxTree ast) {
        address = 0xC;
        this.globals = new Environment();
        this.ast = Objects.requireNonNull(ast);
        output = new StringBuilder();
    }

    public void generate() {
        ast.getHead().accept(this);
        try (PrintStream stream = new PrintStream(new FileOutputStream("a.out"))) {
            stream.print(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visitModuleDeclaration(final ModuleDecl declaration) {
        for (IDecl decl : declaration.getDeclarations()) {
            decl.accept(this);
        }
        if (declaration.hasStatements()) {
            declaration.getStatements().accept(this);
        }
    }

    @Override
    public void visitVariableDeclaration(final VariableDecl declaration) {
        for (String identifier : declaration) {
            if (declaration.isConst()) {
                globals.add(identifier, new LiteralValue(declaration.get(identifier)));
            } else {
                globals.add(identifier, new MemoryAddressValue(address++));
            }
        }
    }

    @Override
    public void visitProcedureDeclaration(final ProcedureDecl declaration) {

    }

    @Override
    public void visitParameterDeclaration(final ParameterDecl declaration) {

    }

    @Override
    public void visitBlockStatement(final BlockStmt statement) {
        // TODO
        for (IStmt stmt : statement) {
            stmt.accept(this);
        }
    }

    @Override
    public void visitIfStatement(final IfStmt statement) {
        for (Map.Entry<IExpr, IStmt> guardedStatement : statement) {

            guardedStatement.getKey().accept(this);
        }
    }

    @Override
    public void visitWhileStatement(final WhileStmt statement) {

    }

    @Override
    public void visitRepeatStatement(final RepeatStmt statement) {
        int start = line;
        statement.getStatements().accept(this);
        if (statement.hasGuard()) {
            statement.getGuard().accept(this);
        }
        output.append(line++).append(" GOTO ").append(start).append("\n");
    }

    @Override
    public void visitReturnStatement(final ReturnStmt statement) {

    }

    @Override
    public void visitExpressionStatement(final ExpressionStmt statement) {
        statement.getExpression().accept(this);
    }

    @Override
    public void visitAssignmentExpression(AssignmentExpr expression) {
        expression.getRight().accept(this);
        int value;
        String mnemonic;
        if (stackTop instanceof LiteralValue) {
            if (stackTop.getPayload() == 0) {
                expression.getLeft().accept(this);
                output.append(line++).append(" CLRF  1 ").append(stackTop.getPayload()).append("\n");
                return;
            } else {
                mnemonic = " MOVLW   ";
                value = stackTop.getPayload();
            }
        } else {
            value = stackTop.getPayload();
            mnemonic = " MOVFW 0 ";
        }
        output.append(line++).append(mnemonic).append(value).append("\n");
        expression.getLeft().accept(this);
        output.append(line++).append(" MOVWF 1 ").append(stackTop.getPayload()).append("\n");
    }

    @Override
    public void visitLogicalExpression(LogicalExpr expression) {
    }

    @Override
    public void visitComparisonExpression(ComparisonExpr expression) {
    }

    @Override
    public void visitArithmeticExpression(ArithmeticExpr expression) {
    }

    @Override
    public void visitUnaryExpression(final UnaryExpr expression) {
    }

    @Override
    public void visitCallExpression(final CallExpr expression) {
        if (expression.hasArgument()) {
            expression.getArgument().accept(this);
            output.append(line++).append(" MOVFW 0 ").append(stackTop.getPayload()).append("\n");
        }
        expression.getCallee().accept(this);
        output.append(line++).append(" CALL ").append(stackTop.getPayload()).append("\n");
    }

    @Override
    public void visitGetExpression(final GetExpr expression) {
        expression.getLeft().accept(this);
        int register = stackTop.getPayload();
        expression.getIndex().accept(this);
        int bit = stackTop.getPayload();
        output.append(bit).append(" ").append(register);
    }

    @Override
    public void visitVariableExpression(final VariableExpr expression) {
        stackTop = globals.get((String) expression.getIdentifier().getValue());
    }

    @Override
    public void visitLiteralExpression(final LiteralExpr expression) {
        stackTop = new LiteralValue(expression.getValue());
    }

}
