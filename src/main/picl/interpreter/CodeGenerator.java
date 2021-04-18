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
import main.picl.scanner.Token;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

public class CodeGenerator implements IVisitor {

    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("./programs/IfStatements.mod"));
        IParser<INode> parser = new Parser(new String(bytes));
        CodeGenerator generator = new CodeGenerator((SyntaxTree) parser.parse());
        generator.generate();
    }

    private int line;
    private int address; // TODO move to Environment
    private final SyntaxTree ast;
    private final Environment globals;
    private IValue stackTop;
    private final StringBuilder output;
    private Map<Integer, String> gotoLocations;

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
        for (IStmt stmt : statement) {
            stmt.accept(this);
        }
    }

    // TODO Check Assignment Order of prints may be buggy
    @Override
    public void visitIfStatement(final IfStmt statement) {
        Iterator<Entry<IExpr, IStmt>> statementIterator = statement.iterator();
        ArrayList<Integer> elseIfLocations = new ArrayList<>();
        gotoLocations = new LinkedHashMap<>();
        while (statementIterator.hasNext()) {
            Entry<IExpr, IStmt> guardedStatement = statementIterator.next();
            IExpr expression = guardedStatement.getKey();
            // ELSE token will not have a key therefore only evaluate the value
            if (expression != null) {
                expression.accept(this);
                if (stackTop instanceof MemoryAddressValue) {
                    output.append(line++).append(" BTFSS 0 ").append(stackTop.getPayload()).append("\n");
                }
                output.append(line++).append(" GOTO ");
                int pos = output.length();
                output.append("\n");
                for (Entry<Integer, String> entry : gotoLocations.entrySet()) {
                    int conditionalPos = entry.getKey();
                    String conditionalType = entry.getValue();
                    if (conditionalType.equalsIgnoreCase("then")) {
                        output.insert(conditionalPos, line);
                    } else {
                        output.insert(conditionalPos, line + 1);
                    }
                }
                guardedStatement.getValue().accept(this);
                // For every ELSEIF statement we nee to add a GOTO at the end to point to the
                // end of the IFStmt
                if (statementIterator.hasNext()) {
                    output.append(line++).append(" GOTO ");
                    elseIfLocations.add(output.length());
                    output.append("\n");
                }
                output.insert(pos, line);
            } else {
                guardedStatement.getValue().accept(this);
            }

        }
        // Update the line number for all ELSEIF GOTO statements
        output.append("\n");
        for (int pos : elseIfLocations) {
            output.insert(pos, line);
        }
    }

    @Override
    public void visitWhileStatement(final WhileStmt statement) {
        Iterator<Entry<IExpr, IStmt>> statementIterator = statement.iterator();
        int initialLine = this.line;
        while (statementIterator.hasNext()) {
            Entry<IExpr, IStmt> guardedStatement = statementIterator.next();
            guardedStatement.getKey().accept(this);
            guardedStatement.getValue().accept(this);
        }
        output.append(line++).append(" GOTO ").append(initialLine).append("\n\n");
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
        expression.getLeft().accept(this);
        if (expression.getOperator() == Token.TokenType.OR) {
            output.append(line++).append(" GOTO ");
            gotoLocations.put(output.length(), "then");
            output.append("\n");
        } else {
            output.append(line++).append(" GOTO ");
            gotoLocations.put(output.length(), "end");
            output.append(("\n"));
        }
        expression.getRight().accept(this);
    }

    @Override
    public void visitComparisonExpression(ComparisonExpr expression) {
        String test = null;
        IValue first = null;
        Enum<?> operator = expression.getOperator();
        if (operator == Token.TokenType.EQL) {
            //MOVFW 0 RightAddress
            expression.getRight().accept(this);
            first = stackTop;
            test = " BTFSS 2 3\n";
            //SUBWF 0 LeftAddress
            expression.getLeft().accept(this);
            //BTFSS 2 3
        } else if (operator == Token.TokenType.NEQ) {
            test = " BTFSC 2 3\n";
            //MOVFW 0 RightAddress
            expression.getRight().accept(this);
            first = stackTop;
            //SUBWF 0 LeftAddress
            expression.getLeft().accept(this);
            //BTFSC 2 3
        } else if (operator == Token.TokenType.GTR) {
            test = " BTFSS 0 3\n";
            //MOVFW 0 LeftAddress
            expression.getLeft().accept(this);
            first = stackTop;
            //SUBWF 0 RightAddress
            expression.getRight().accept(this);
            //BTFSC 0 3
        } else if (operator == Token.TokenType.GEQ) {
            test = " BTFSS 0 3\n";
            //MOVFW 0 RightAddress
            expression.getRight().accept(this);
            first = stackTop;
            //SUBWF 0 LeftAddress
            expression.getLeft().accept(this);
            //BTFSS 0 3
        } else if (operator == Token.TokenType.LSS) {
            test = " BTFSS 0 3\n";
            //MOVFW 0 RightAddress
            expression.getRight().accept(this);
            first = stackTop;
            //SUBWF 0 LeftAddress
            expression.getLeft().accept(this);
            //BTFSS 0 3
        } else if (operator == Token.TokenType.LEQ) {
            test = " BTFSC 0 3\n";
            //MOVFW 0 RightAddress
            expression.getRight().accept(this);
            first = stackTop;
            //SUBWF 0 LeftAddress
            expression.getLeft().accept(this);
            //BTFSC 0 3
        }
        if (test != null && first != null) {
            output.append(line++).append(" MOVFW 0 ").append(first.getPayload());
            output.append(line++).append(" SUBWF 0 ").append(stackTop.getPayload());
            output.append(line++).append(test);
        } else {
            throw new Error(); // TODO need picl error
        }
    }

    @Override
    public void visitArithmeticExpression(ArithmeticExpr expression) {
        output.append(line++).append(" Arithmetic Unimplemented \n");
    }

    @Override
    public void visitUnaryExpression(final UnaryExpr expression) {
        output.append(line++).append(" Unary Unimplemented \n");
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
