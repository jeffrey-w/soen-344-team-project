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

import static main.picl.scanner.Token.TokenType.*;

public class CodeGenerator implements IVisitor {

    private enum BinaryOperationConfiguration {
        LITERAL_LITERAL,
        LITERAL_VARIABLE_INT,
        LITERAL_VARIABLE_SET,
        VARIABLE_LITERAL_INT,
        VARIABLE_LITERAL_SET,
        VARIABLE_VARIABLE_INT,
        VARIABLE_VARIABLE_SET
    }

    private static final boolean THEN = true, END = false;

    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("./programs/WhileStatements.mod"));
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
    private Map<Integer, Boolean> gotoLocations;
    private boolean isGuard;
    private boolean isAssignment;
    private boolean isArithmetic;
    private boolean isWaitFalse;
    private boolean isClear;
    private BinaryOperationConfiguration configuration;
    private final Deque<IValue> stack;

    public CodeGenerator(SyntaxTree ast) {
        address = 0xC;
        this.globals = new Environment();
        this.ast = Objects.requireNonNull(ast);
        output = new StringBuilder();
        stack = new ArrayDeque<>();
        // TODO put this elsewhere
        globals.add("A", new MemoryAddressValue(5, null));
        globals.add("B", new MemoryAddressValue(6, null));
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
                globals.add(identifier, new MemoryAddressValue(address++, declaration.getType()));
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
                isGuard = true;
                expression.accept(this);
                isGuard = false;
                output.append(line++).append(" GOTO ");
                int pos = output.length() + (gotoLocations.isEmpty() ? 0 : 4), index = 0;
                output.append("\n");
                for (Entry<Integer, Boolean> entry : gotoLocations.entrySet()) {
                    int conditionalPos = entry.getKey() + index++ * gotoLocations.size();
                    if (entry.getValue()) {
                        output.insert(conditionalPos, line);
                    } else {
                        output.insert(conditionalPos, line + 1);
                    }
                }
                guardedStatement.getValue().accept(this);
                // For every ELSEIF statement we nee to add a GOTO at the end to point to the
                // end of the IF statement
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
        int index = 0;
        for (int pos : elseIfLocations) {
            output.insert(pos + index++ * elseIfLocations.size() + 2, line);
        }
    }

    @Override
    public void visitWhileStatement(final WhileStmt statement) {
        gotoLocations = new LinkedHashMap<>();
        Iterator<Entry<IExpr, IStmt>> statementIterator = statement.iterator();
        int initialLine = this.line;
        while (statementIterator.hasNext()) {
            Entry<IExpr, IStmt> guardedStatement = statementIterator.next();
            isGuard = true;
            guardedStatement.getKey().accept(this);
            isGuard = false;
            output.append(line++).append(" GOTO ");
            int pos = output.length();
            output.append("\n");
            guardedStatement.getValue().accept(this);
            output.insert(pos, line + 1);
            // TODO GOTO 23 instead of 22 on line 19
            int index = 0;
            for (Entry<Integer, Boolean> entry : gotoLocations.entrySet()) {
                int conditionalPos = entry.getKey() + index++ * gotoLocations.size();
                if (entry.getValue()) {
                    output.insert(conditionalPos, line);
                } else {
                    output.insert(conditionalPos, line + 1);
                }
            }
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
        isAssignment = true;
        expression.getLeft().accept(this);
        IValue left = stackTop;
        expression.getRight().accept(this);
        if (isAssignment) {
            int value;
            String mnemonic;
            if (stackTop instanceof LiteralValue && !isArithmetic) {
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
            if (!isArithmetic) {
                output.append(line++).append(mnemonic).append(value).append("\n");
            }
            output.append(line++).append(" MOVWF 1 ").append(left.getPayload()).append("\n");
            isArithmetic = false;
        }
    }

    @Override
    public void visitLogicalExpression(LogicalExpr expression) {
        expression.getLeft().accept(this);
        if (expression.getOperator() == Token.TokenType.OR) {
            output.append(line++).append(" GOTO ");
            gotoLocations.put(output.length(), THEN);
            output.append("\n");
        } else {
            output.append(line++).append(" GOTO ");
            gotoLocations.put(output.length(), END);
            output.append(("\n"));
        }
        expression.getRight().accept(this);
    }

    @Override
    public void visitComparisonExpression(ComparisonExpr expression) {
        isGuard = false;
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
            //MOVFW 0 LeftAddress
            expression.getLeft().accept(this);
            first = stackTop;
            //SUBWF 0 RightAddress
            expression.getRight().accept(this);
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
            output.append(line++).append(" MOVFW 0 ").append(first.getPayload()).append("\n");
            if (!(stackTop instanceof LiteralValue && stackTop.getPayload() == 0)) {
                output.append(line++).append(" SUBWF 0 ").append(stackTop.getPayload()).append("\n");
            }
            output.append(line++).append(test).append("\n");
        } else {
            throw new Error(); // TODO need picl error
        }
        isGuard = true;
    }

    @Override
    public void visitArithmeticExpression(ArithmeticExpr expression) {
        IValue last = stackTop;
        setupBinaryOperation(expression);
        IValue left = stack.pop();
        IValue right = stack.pop();
        boolean isSelfAssignment = isAssignment && (left instanceof MemoryAddressValue && left.getPayload() == last.getPayload());
        isArithmetic = true;
        switch (configuration) {
            case LITERAL_LITERAL:
                // TODO
                break;
            case LITERAL_VARIABLE_INT:
                if (expression.getOperator() == PLUS) {
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" ADDLW ").append(left.getPayload()).append("\n");
                } else if (expression.getOperator() == MINUS) {
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" SUBLW ").append(left.getPayload()).append("\n");
                } // TODO not addition or subtraction error
                break;
            case LITERAL_VARIABLE_SET:
                if (expression.getOperator() == PLUS) {
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" IORLW 0 ").append(left.getPayload()).append("\n");
                } else if (expression.getOperator() == MINUS) {
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" XORLW 0 ").append(left.getPayload()).append("\n");
                } else if (expression.getOperator() == AST) {
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" ANDLW 0 ").append(left.getPayload()).append("\n");
                } // TODO other error
                break;
            case VARIABLE_LITERAL_INT:
                if (expression.getOperator() == PLUS) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVLW ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" ADDWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                } else if (expression.getOperator() == MINUS) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVLW ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" SUBWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                } // TODO other error
                break;
            case VARIABLE_LITERAL_SET:
                if (expression.getOperator() == PLUS) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVLW ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" IORWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                } else if (expression.getOperator() == MINUS) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVLW ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" XORWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                } else if (expression.getOperator() == AST) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVLW ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" ANDWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                }
                break;
            case VARIABLE_VARIABLE_INT:
                if (expression.getOperator() == PLUS) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" ADDWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                } else if (expression.getOperator() == MINUS) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" SUBWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                } // TODO other error
                break;
            case VARIABLE_VARIABLE_SET:
                if (expression.getOperator() == PLUS) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" IORWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                } else if (expression.getOperator() == MINUS) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" XORWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                } else if (expression.getOperator() == AST) {
                    int d = isSelfAssignment ? 1 : 0;
                    output.append(line++).append(" MOVFW 0 ").append(right.getPayload()).append("\n");
                    output.append(line++).append(" ANDWF ").append(d).append(" ").append(left.getPayload()).append("\n");
                    isAssignment = d == 0;
                }
                break;
        }
    }

    private void setupBinaryOperation(BinaryExpr expression) {
        expression.getRight().accept(this);
        IValue right = stackTop;
        expression.getLeft().accept(this);
        IValue left = stackTop;
        if (left instanceof LiteralValue && right instanceof LiteralValue) {
            configuration = BinaryOperationConfiguration.LITERAL_LITERAL;
        } else if (left instanceof LiteralValue) {
            MemoryAddressValue addressValue = (MemoryAddressValue) right;
            if (addressValue.getType() == INT) {
                 configuration = BinaryOperationConfiguration.LITERAL_VARIABLE_INT;
            } else {
                configuration = BinaryOperationConfiguration.LITERAL_VARIABLE_SET;
            }
        } else if (right instanceof LiteralValue) {
            MemoryAddressValue addressValue = (MemoryAddressValue) left;
            if (addressValue.getType() == INT) {
                configuration = BinaryOperationConfiguration.VARIABLE_LITERAL_INT;
            } else {
                configuration = BinaryOperationConfiguration.VARIABLE_LITERAL_SET;
            }
        } else {
            MemoryAddressValue addressValue = (MemoryAddressValue) left;
            if (addressValue.getType() == INT) {
                configuration = BinaryOperationConfiguration.VARIABLE_VARIABLE_INT;
            } else {
                configuration = BinaryOperationConfiguration.VARIABLE_VARIABLE_SET;
            }
        }
        stack.push(right);
        stack.push(left);
    }

    @Override
    public void visitUnaryExpression(final UnaryExpr expression) {
        switch ((Token.TokenType) expression.getOperator()) {
            case QUERY:
                if (isWaitFalse = isWaitFalse(expression)) {
                    output.append(line++).append(" BTFSC ");
                } else {
                    output.append(line++).append(" BTFSS ");
                }
                expression.getOperand().accept(this); // Maybe variable or get
                if (!isWaitFalse) {
                    if (!(expression.getOperand() instanceof GetExpr)) {
                        output.append(0 + " ").append(stackTop.getPayload()).append("\n");
                    } else {
                        output.append("\n");
                    }
                }
                output.append(line++).append(" GOTO ").append(line - 2).append("\n");
                isWaitFalse = false;
                break;
            case OP:
                if (isClear = isClear(expression)) {
                    output.append(line++).append(" BCF ");
                } else {
                    output.append(line++).append(" BSF ");
                }
                expression.getOperand().accept(this); // Maybe variable or get
                if (!isClear) {
                    if (!(expression.getOperand() instanceof GetExpr)) {
                        outPutVariable();
                    } else {
                        output.append("\n");
                    }
                }
                isClear = false;
                break;
            case NOT:
                if (!(isClear || isWaitFalse)) { // Just a NOT. TODO does this ever happen?
                    // TODO
                }
                expression.getOperand().accept(this); // Maybe variable or get
                if (!(expression.getOperand() instanceof GetExpr) && !isGuard) { // TODO check for isGuard elsewhere
                    outPutVariable();
                } else {
                    output.append("\n");
                }
                break;
            case INC:
                expression.getOperand().accept(this);
                output.append(line++).append(" INCF 1 ").append(stackTop.getPayload()).append("\n");
                break;
            case DEC:
                expression.getOperand().accept(this);
                output.append(line++).append(" DECF 1 ").append(stackTop.getPayload()).append("\n");
                break;
            case ROL:
                expression.getOperand().accept(this);
                output.append(line++).append(" RFL 1 ").append(stackTop.getPayload()).append("\n");
                break;
            case ROR:
                expression.getOperand().accept(this);
                output.append(line++).append(" RRF 1 ").append(stackTop.getPayload()).append("\n");
                break;
            default:
                throw new Error(); // TODO need picl error
        }
    }

    private boolean isWaitFalse(UnaryExpr expression) {
        if (expression.getOperand() instanceof UnaryExpr) {
            return ((UnaryExpr) expression.getOperand()).getOperator() == NOT;
        }
        return false;
    }

    private boolean isClear(UnaryExpr expression) {
        if (expression.getOperand() instanceof  UnaryExpr) {
            return ((UnaryExpr) expression.getOperand()).getOperator() == NOT;
        }
        return false;
    }

    private void outPutVariable() {
        output.append(0 + " ").append(stackTop.getPayload()).append("\n");
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
        if (isGuard) {
            output.append(line++).append(" BTFSS 0 ").append(stackTop.getPayload()).append("\n");
        }
    }

    @Override
    public void visitLiteralExpression(final LiteralExpr expression) {
        stackTop = new LiteralValue(expression.getValue());
    }

}
