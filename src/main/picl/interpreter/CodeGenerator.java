package main.picl.interpreter;

import main.parser.Environment;
import main.parser.IParser;
import main.picl.interpreter.decl.*;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;
import main.picl.parser.Parser;
import main.picl.parser.SyntaxTree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Map.Entry;

public class CodeGenerator implements IVisitor {

    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("./programs/IfStatements.mod"));
        IParser parser = new Parser(new String(bytes));
        CodeGenerator generator = new CodeGenerator((SyntaxTree) parser.parse());
        generator.generate();
    }

    private int address = 12;
    private int line;
    private Environment globals;
    private SyntaxTree ast;
    private Object stackTop;
    private PrintWriter stream;
    private RandomAccessFile randomAccessFile = new RandomAccessFile("a.out", "rws");

    public CodeGenerator(SyntaxTree ast) throws FileNotFoundException {
        this.globals = new Environment();
        this.ast = Objects.requireNonNull(ast);
        stream = new PrintWriter(new FileOutputStream("a.out"));
    }

    public void generate() {
        ast.getHead().accept(this);
        stream.close();
    }

    @Override
    public void visitModuleDeclaration(final ModuleDecl declaration) {
        // TODO
        for (IDecl decl : declaration.getDeclarations()) {
            decl.accept(this);
        }
        if (declaration.hasStatements()) {
            declaration.getStatements().accept(this);
        }
    }

    @Override
    public void visitVariableDeclaration(final VariableDecl declaration) {
        // TODO
        for (String identifier : declaration) {
            if (declaration.isConst()) {
                globals.add(identifier, new Environment.EntryInfo(null, declaration.get(identifier)));
            } else {
                globals.add(identifier, new Environment.EntryInfo(declaration.getType(), address++));
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
        Iterator<Entry<IExpr, IStmt>> statementIterator = statement.iterator();
        ArrayList<Long> elseIfLocations = new ArrayList<Long>();
        while (statementIterator.hasNext()) {
            Entry<IExpr, IStmt> guardedStatement = statementIterator.next();
            IExpr expression = guardedStatement.getKey();

            // ELSE token will not have a key therefore only evaluate the value
            if (expression != null) {
                expression.accept(this);
                try {
                    randomAccessFile
                            .write((line++ + " BTFSS 0 " + ((Environment.EntryInfo) stackTop).value + "\n").getBytes());
                    randomAccessFile.write((line++ + " GOTO ").getBytes());
                    long pos = randomAccessFile.getFilePointer();
                    randomAccessFile.write("  \n".getBytes()); // TODO generalize number of leading spaces

                    guardedStatement.getValue().accept(this);
                    
                    // For every ELSEIF statement we nee to add a GOTO at the end to point to the
                    // end of the IFStmt
                    if (statementIterator.hasNext()) {
                        randomAccessFile.write((line++ + " GOTO ").getBytes());
                        elseIfLocations.add(randomAccessFile.getFilePointer());
                        randomAccessFile.write("  \n".getBytes());
                    }
                    randomAccessFile.seek(pos);
                    randomAccessFile.write(String.valueOf(line).getBytes());
                    randomAccessFile.seek(randomAccessFile.length());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                guardedStatement.getValue().accept(this);
            }

        }

        // Update the line number for all ELSEIF GOTO statements
        try {
            randomAccessFile.write("\n".getBytes());
            for (long pos : elseIfLocations) {
                randomAccessFile.seek(pos);
                randomAccessFile.write(String.valueOf(line).getBytes());
                randomAccessFile.seek(randomAccessFile.length());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void tryWriteBytes(String bytes) {
        // TODO
    }

    @Override
    public void visitWhileStatement(final WhileStmt statement) {

    }

    @Override
    public void visitRepeatStatement(final RepeatStmt statement) {

    }

    @Override
    public void visitReturnStatement(final ReturnStmt statement) {

    }

    @Override
    public void visitExpressionStatement(final ExpressionStmt statement) {
        // TODO
        statement.getExpression().accept(this);
    }

    @Override
    public void visitAssignmentExpression(AssignmentExpr expression) {
        // TODO
        try {
            expression.getRight().accept(this);
            Object value = null;
            String mnemonic = null;
            if (stackTop instanceof Integer && (Integer) stackTop == 0) {
                expression.getLeft().accept(this);
                randomAccessFile
                        .write((line++ + " CLRF " + ((Environment.EntryInfo) stackTop).value + "\n").getBytes());
                return;
            } else if (stackTop instanceof Integer) {
                mnemonic = " MOVLW ";
                value = stackTop;
            } else if (stackTop instanceof Environment.EntryInfo) {
                value = ((Environment.EntryInfo) stackTop).value;
                if (((Environment.EntryInfo) stackTop).type == null) {
                    mnemonic = " MOVLW ";
                } else {
                    mnemonic = " MOVFW ";
                }
            }
            randomAccessFile.write((line++ + mnemonic + value + "\n").getBytes());
            expression.getLeft().accept(this);
            randomAccessFile.write((line++ + " MOVWF " + ((Environment.EntryInfo) stackTop).value + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visitLogicalExpression(LogicalExpr expression) {
    }

    @Override
    public void visitComparisonExpression(ComparisonExpr expression) {

    }

    @Override
    public void visitArithmeticExpression(ArithmeticExpr expression) {
        try {
            randomAccessFile.write((line++ + " Arithmetic Unimplemented \n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void visitUnaryExpression(final UnaryExpr expression) {

    }

    @Override
    public void visitCallExpression(final CallExpr expression) {

    }

    @Override
    public void visitGetExpression(final GetExpr expression) {

    }

    @Override
    public void visitVariableExpression(final VariableExpr expression) {
        stackTop = globals.get((String) expression.getIdentifier().getValue());
    }

    @Override
    public void visitLiteralExpression(final LiteralExpr expression) {
        stackTop = expression.getValue();
    }

}
