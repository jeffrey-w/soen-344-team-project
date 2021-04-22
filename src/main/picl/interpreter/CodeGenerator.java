package main.picl.interpreter;

import main.parser.Environment;
import main.parser.ISyntaxTree;
import main.parser.IValue;
import main.picl.interpreter.decl.*;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;
import main.picl.parser.LiteralValue;
import main.picl.parser.MemoryAddressValue;
import main.picl.parser.ProcedureValue;
import main.picl.scanner.Token;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.Map.Entry;

import static main.picl.scanner.Token.TokenType.*;

public class CodeGenerator implements IVisitor {
    private static class Flag {
        private boolean isGuard;
        private boolean isAssignment;
        private boolean isArithmetic;
        private boolean isDecrement;
        private boolean isSelfAssignment;
        private boolean isReturn;
        private boolean isNot;
    }


    private enum ArithmeticOperationConfiguration {
        LITERAL_LITERAL,
        LITERAL_VARIABLE_INT,
        LITERAL_VARIABLE_SET,
        VARIABLE_LITERAL_INT,
        VARIABLE_LITERAL_SET,
        VARIABLE_VARIABLE_INT,
        VARIABLE_VARIABLE_SET
    }

    private static ArithmeticOperationConfiguration configurationOf(IValue left, IValue right) {
        if (left instanceof LiteralValue && right instanceof LiteralValue) {
            return ArithmeticOperationConfiguration.LITERAL_LITERAL;
        }
        if (left instanceof LiteralValue) {
            MemoryAddressValue addressValue = (MemoryAddressValue) right;
            if (addressValue.getType() == INT) {
                return ArithmeticOperationConfiguration.LITERAL_VARIABLE_INT;
            } else {
                return ArithmeticOperationConfiguration.LITERAL_VARIABLE_SET;
            }
        }
        MemoryAddressValue addressValue = (MemoryAddressValue) left;
        if (right instanceof LiteralValue) {
            if (addressValue.getType() == INT) {
                return ArithmeticOperationConfiguration.VARIABLE_LITERAL_INT;
            } else {
                return ArithmeticOperationConfiguration.VARIABLE_LITERAL_SET;
            }
        } else {
            if (addressValue.getType() == INT) {
                return ArithmeticOperationConfiguration.VARIABLE_VARIABLE_INT;
            } else {
                return ArithmeticOperationConfiguration.VARIABLE_VARIABLE_SET;
            }
        }
    }

    private static final boolean THEN = true, END = false;

    private int line;
    private int address; // TODO move to Environment
    private final ISyntaxTree<INode> ast;
    private Environment globals;
    private IValue result;
    private Flag flags;
    private final StringBuilder output;
    private Map<Integer, Boolean> gotoLocations;
    
    private final String fileName;

    public CodeGenerator(ISyntaxTree<INode> ast, String fileName) {
        address = 0xC;
        this.globals = new Environment();
        this.ast = Objects.requireNonNull(ast);
        output = new StringBuilder();
        addPorts();
        this.fileName = fileName != null ? fileName : "a.out";
    }

    private void addPorts() {
        globals.add("A", new MemoryAddressValue(5, null));
        globals.add("B", new MemoryAddressValue(6, null));
    }
    
    public void generate() {
        ast.getHead().accept(this);
        try (PrintStream stream = new PrintStream(new FileOutputStream(fileName))) {
            stream.print(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visitModuleDeclaration(final ModuleDecl declaration) {
        int pos = 0;
        if (declaration.hasProcedures()) {
            pos = writeGoto();
            newLine();
        }
        for (IDecl decl : declaration.getDeclarations()) {
            decl.accept(this);
        }
        if (declaration.hasProcedures()) {
            output.insert(pos, line);
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
        globals = new Environment(globals);
        int start = line;
        if(declaration.hasParameter()) {
            writeLine("MOVFW 1 " + address);
            declaration.getParameter().accept(this);
        }
        for (IDecl decl : declaration.getDeclarations()){
            decl.accept(this);
        }
        if (declaration.hasStatements()) {
            declaration.getStatements().accept(this);
        }
        if(declaration.hasReturnStatement()){
            declaration.getReturnStatement().accept(this);
        }
        writeLine("RET");
        IValue procedure = new ProcedureValue(start);
        globals = globals.getParent();
        globals.add(declaration.getIdentifier(), procedure);
    }

    @Override
    public void visitParameterDeclaration(final ParameterDecl declaration) {
        globals.add(declaration.getIdentifier(), new MemoryAddressValue(address++, declaration.getType()));
    }

    @Override
    public void visitBlockStatement(final BlockStmt statement) {
        for (IStmt stmt : statement) {
            stmt.accept(this);
        }
    }
    
    private void revisitGotoLocations(Boolean ifStatement) {
        int index = 0;
        for (Entry<Integer, Boolean> entry : gotoLocations.entrySet()) {
            int conditionalPos = entry.getKey() + index++ * gotoLocations.size();
            if (entry.getValue()) {
                output.insert(conditionalPos, ifStatement ? line : line - 1);
            } else {
                output.insert(conditionalPos, line + 1);
            }
        }
    }

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
                evaluateGuard(expression);
                int pos = writeGoto() + (gotoLocations.isEmpty() ? 0 : 4);
                newLine();
                revisitGotoLocations(true);
                guardedStatement.getValue().accept(this);
                // For every ELSEIF statement we nee to add a GOTO at the end to point to the
                // end of the IF statement
                if (statementIterator.hasNext()) {
                    elseIfLocations.add(writeGoto());
                    newLine();
                }
                output.insert(pos, line);
            } else {
                guardedStatement.getValue().accept(this);
            }

        }
        // Update the line number for all ELSEIF GOTO statements
        int index = 0;
        for (int pos : elseIfLocations) {
            output.insert(pos + index++ * elseIfLocations.size() + 2, line);
        }
    }

    @Override
    public void visitWhileStatement(final WhileStmt statement) {
        gotoLocations = new LinkedHashMap<>();
        Iterator<Entry<IExpr, IStmt>> statementIterator = statement.iterator();
        int start = this.line;
        while (statementIterator.hasNext()) {
            Entry<IExpr, IStmt> guardedStatement = statementIterator.next();
            flags.isSelfAssignment = false;
            evaluateGuard(guardedStatement.getKey());
            int pos = writeGoto();
            newLine();
            guardedStatement.getValue().accept(this);
            output.insert(pos, line + 1);
            revisitGotoLocations(false);
        }
        writeLine("GOTO " + start);
        newLine();
    }

    @Override
    public void visitRepeatStatement(final RepeatStmt statement) {
        int start = line;
        statement.getStatements().accept(this);
        if (statement.hasGuard()) {
            IExpr variable;
            if ((variable = statement.isSpecial(flags.isDecrement)) != null) {
                output.delete(output.lastIndexOf("DECF"), output.length());
                variable.accept(this);
                write("DECFSZ 1 " + result.getPayload());
                newLine();
            } else {
                evaluateGuard(statement.getGuard());
            }
        }
        writeLine("GOTO " + start);
    }

    @Override
    public void visitReturnStatement(final ReturnStmt statement) {
        statement.getExpression().accept(this);
        writeLine("MOVFW 0 " + result.getPayload());
    }

    @Override
    public void visitExpressionStatement(final ExpressionStmt statement) {
        statement.getExpression().accept(this);
        flags.isDecrement = statement.getExpression() instanceof UnaryExpr
                && ((UnaryExpr) statement.getExpression()).getOperator() == DEC;
    }

    @Override
    public void visitAssignmentExpression(AssignmentExpr expression) {
        flags.isAssignment = true;
        expression.getLeft().accept(this);
        IValue left = result;
        expression.getRight().accept(this);
        if (flags.isAssignment) {
            int value;
            String mnemonic;
            if (result.isImmediate() && !flags.isArithmetic) {
                if (result.getPayload() == 0) {
                    expression.getLeft().accept(this);
                    writeLine("CLRF 1 " + result.getPayload());
                    return;
                } else {
                    mnemonic = "MOVLW ";
                    value = result.getPayload();
                }
            } else {
                value = result.getPayload();
                mnemonic = "MOVFW 0 ";
            }
            if (!flags.isArithmetic && !flags.isReturn) {
                writeLine(mnemonic + value);
            }
            writeLine("MOVFW 1 " + left.getPayload());
            flags.isArithmetic = false;
            flags.isReturn = false;
        }
    }

    @Override
    public void visitLogicalExpression(LogicalExpr expression) {
        expression.getLeft().accept(this);
        if (expression.getOperator() == Token.TokenType.OR) {
            gotoLocations.put(writeGoto(), THEN);
        } else {
            gotoLocations.put(writeGoto(), END);
        }
        newLine();
        expression.getRight().accept(this);
    }

    @Override
    public void visitComparisonExpression(ComparisonExpr expression) {
        boolean wasGuard = flags.isGuard;
        flags.isGuard = false;
        String test = null;
        IValue first = null;
        Enum<?> operator = expression.getOperator();
        if (operator == Token.TokenType.EQL) {
            expression.getRight().accept(this);
            first = result;
            test = "BTFSS 2 3";
            expression.getLeft().accept(this);
        } else if (operator == Token.TokenType.NEQ) {
            test = "BTFSC 2 3";
            expression.getLeft().accept(this);
            first = result;
            expression.getRight().accept(this);
        } else if (operator == Token.TokenType.GTR) {
            test = "BTFSS 0 3";
            expression.getLeft().accept(this);
            first = result;
            expression.getRight().accept(this);
        } else if (operator == Token.TokenType.GEQ) {
            test = "BTFSS 0 3";
            expression.getRight().accept(this);
            first = result;
            expression.getLeft().accept(this);
        } else if (operator == Token.TokenType.LSS) {
            test = "BTFSS 0 3";
            expression.getRight().accept(this);
            first = result;
            expression.getLeft().accept(this);
        } else if (operator == Token.TokenType.LEQ) {
            test = "BTFSC 0 3";
            expression.getRight().accept(this);
            first = result;
            expression.getLeft().accept(this);
        }
        flags.isGuard = wasGuard;
        if (first.isImmediate()) {
            writeLine("MOVFW 0 " + result.getPayload());
            if (!(result.isImmediate() && result.getPayload() == 0) && !flags.isSelfAssignment) {
                writeLine("SUBFW 0" + first.getPayload());
            }
            writeLine(test);
        } else {
            if (test != null && first != null) {
                writeLine("MOVFW 0 " + first.getPayload());
                if (!(result.isImmediate() && result.getPayload() == 0) && !flags.isSelfAssignment) {
                    writeLine("SUBFW 0 " + result.getPayload());
                }
                writeLine(test);
            } else {
                throw new Error(); // TODO need picl error
            }
        } // TODO other combinations
    }

    @Override
    public void visitArithmeticExpression(ArithmeticExpr expression) {
        IValue previous = result;
        expression.getLeft().accept(this);
        IValue left = result;
        expression.getRight().accept(this);
        IValue right = result;
        flags.isSelfAssignment = isSelfAssignment(previous, left);
        flags.isArithmetic = true;
        int d = flags.isSelfAssignment ? 1 : 0;
        switch (configurationOf(left, right)) {
            case LITERAL_LITERAL:
                // TODO
                break;
            case LITERAL_VARIABLE_INT:
                writeLine("MOVFW 0" + right.getPayload());
                if (expression.getOperator() == PLUS) {
                    writeLine("ADDLW " + left.getPayload());
                } else if (expression.getOperator() == MINUS) {
                    writeLine("SUBLW " + left.getPayload());
                }
                break;
            case LITERAL_VARIABLE_SET:
                writeLine("MOVFW 0 " + right.getPayload());
                if (expression.getOperator() == PLUS) {
                    writeLine("IORLW 0 " + left.getPayload());
                } else if (expression.getOperator() == MINUS) {
                    writeLine("XORLW 0" + left.getPayload());
                } else if (expression.getOperator() == AST) {
                    writeLine("ANDLW 0 " + left.getPayload());
                }
                break;
            case VARIABLE_LITERAL_INT: {
                writeLine("MOVLW " + right.getPayload());
                if (expression.getOperator() == PLUS) {
                    writeLine("ADDWF " + d + " " + left.getPayload());
                } else if (expression.getOperator() == MINUS) {
                    writeLine("SUBWF " + d + " " + left.getPayload());
                }
                break;
            }
            case VARIABLE_LITERAL_SET:
                writeLine("MOVLW " + right.getPayload());
                if (expression.getOperator() == PLUS) {
                    writeLine("IORWF " + d + " " + left.getPayload());
                } else if (expression.getOperator() == MINUS) {
                    writeLine("XORWF " + d + " " + left.getPayload());
                } else if (expression.getOperator() == AST) {
                    writeLine("ANDWF " + d + " " + left.getPayload());
                }
                break;
            case VARIABLE_VARIABLE_INT:
                writeLine("MOVWF 0 " + right.getPayload());
                if (expression.getOperator() == PLUS) {
                    writeLine("ADDWF " + d + " " + left.getPayload());
                } else if (expression.getOperator() == MINUS) {
                    writeLine("SUBWF " + d + " " + left.getPayload());
                }
                break;
            case VARIABLE_VARIABLE_SET:
                writeLine("MOVFW 0 " + right.getPayload());
                if (expression.getOperator() == PLUS) {
                    writeLine("IORWF " + d + " " + left.getPayload());
                } else if (expression.getOperator() == MINUS) {
                    writeLine("XORWF " + d + " " + left.getPayload());
                } else if (expression.getOperator() == AST) {
                    writeLine("ANDWF " + d + " " + left.getPayload());
                }
                break;
        }
        flags.isAssignment = !flags.isSelfAssignment;
    }

    private boolean isSelfAssignment(IValue previous, IValue left) {
        return flags.isAssignment && (left.isAddress() && left.getPayload() == previous.getPayload());
    }

    @Override
    public void visitUnaryExpression(final UnaryExpr expression) {
        boolean isWaitFalse, isClear;
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
                        output.append(0 + " ").append(result.getPayload()).append("\n");
                    } else {
                        output.append("\n");
                    }
                }
                output.append(line++).append(" GOTO ").append(line - 2).append("\n");
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
                        output.append(0 + " ").append(result.getPayload()).append("\n");
                    } else {
                        output.append("\n");
                    }
                }
                break;
            case NOT:
                flags.isNot = true;
                expression.getOperand().accept(this); // Maybe variable or get
                flags.isNot = false;
                if (!(expression.getOperand() instanceof GetExpr) && !flags.isGuard) { // TODO check for isGuard elsewhere
                    output.append(0 + " ").append(result.getPayload()).append("\n");
                } else {
                    output.append("\n");
                }
                break;
            case INC:
                expression.getOperand().accept(this);
                output.append(line++).append(" INCF 1 ").append(result.getPayload()).append("\n");
                break;
            case DEC:
                expression.getOperand().accept(this);
                output.append(line++).append(" DECF 1 ").append(result.getPayload()).append("\n");
                break;
            case ROL:
                expression.getOperand().accept(this);
                output.append(line++).append(" RFL 1 ").append(result.getPayload()).append("\n");
                break;
            case ROR:
                expression.getOperand().accept(this);
                output.append(line++).append(" RRF 1 ").append(result.getPayload()).append("\n");
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
        if (expression.getOperand() instanceof UnaryExpr) {
            return ((UnaryExpr) expression.getOperand()).getOperator() == NOT;
        }
        return false;
    }

    @Override
    public void visitCallExpression(final CallExpr expression) {
        if (expression.hasArgument()) {
            expression.getArgument().accept(this);
            output.append(line++).append(" MOVFW 0 ").append(result.getPayload()).append("\n");
        }
        expression.getCallee().accept(this);
        output.append(line++).append(" CALL ").append(result.getPayload()).append("\n");
        flags.isReturn = true;
    }

    @Override
    public void visitGetExpression(final GetExpr expression) {
        expression.getLeft().accept(this);
        int register = result.getPayload();
        expression.getIndex().accept(this);
        int bit = result.getPayload();
        if (bit != 0) {
            output.append(bit).append(" ").append(register);
        }
    }

    @Override
    public void visitVariableExpression(final VariableExpr expression) {
        result = globals.get((String) expression.getIdentifier().getValue());
        if (flags.isGuard) {
            if(flags.isNot){
                output.append(line++).append(" BTFSC 0 ").append(result.getPayload());
            }
            else{
                output.append(line++).append(" BTFSS 0 ").append(result.getPayload()).append("\n");
            }
        }
    }

    @Override
    public void visitLiteralExpression(final LiteralExpr expression) {
        result = new LiteralValue(expression.getValue());
    }

    private void writeLine(String line) {
        output.append(this.line++).append(" ").append(line).append("\n");
    }

    private void newLine() {
        output.append('\n');
    }

    private void write(String line) {
        output.append(line);
    }

    private int writeGoto() {
        return output.append(line++).append(" GOTO ").length();
    }

    private void evaluateGuard(IExpr guard) {
        flags.isGuard = true;
        guard.accept(this);
        flags.isGuard = false;
    }

}
