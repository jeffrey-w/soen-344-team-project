package main.picl.interpreter;

import main.parser.Environment;
import main.parser.IParser;
import main.picl.interpreter.decl.*;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;
import main.picl.parser.Parser;
import main.picl.parser.SyntaxTree;
import main.picl.scanner.Token;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

public class CodeGenerator implements IVisitor {
    private Map<Long,String> gotoLocations = new LinkedHashMap<Long, String>();
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
        try {
            randomAccessFile.write((line++ + " Procedure Unimplemented \n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void visitParameterDeclaration(final ParameterDecl declaration) {
        try {
            randomAccessFile.write((line++ + " Parameter Unimplemented \n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void visitBlockStatement(final BlockStmt statement) {
        // TODO
        for (IStmt stmt : statement) {
            stmt.accept(this);
        }
    }


    // TODO Check Assignment Order of prints may be buggy
    @Override
    public void visitIfStatement(final IfStmt statement) {
        Iterator<Entry<IExpr, IStmt>> statementIterator = statement.iterator();
        ArrayList<Long> elseIfLocations = new ArrayList<Long>();
        gotoLocations = new LinkedHashMap<Long, String>();
        while (statementIterator.hasNext()) {
            Entry<IExpr, IStmt> guardedStatement = statementIterator.next();
            IExpr expression = guardedStatement.getKey();

            // ELSE token will not have a key therefore only evaluate the value
            if (expression != null) {
                expression.accept(this);
                try {
                    if(stackTop instanceof Environment.EntryInfo) {
                        randomAccessFile
                                .write((line++ + " BTFSS 0 " + ((Environment.EntryInfo) stackTop).value + "\n").getBytes());
                    }
                    randomAccessFile.write((line++ + " GOTO ").getBytes());
                    long pos = randomAccessFile.getFilePointer();
                    randomAccessFile.write("  \n".getBytes()); // TODO generalize number of leading spaces
                    for (Map.Entry<Long, String> entry : gotoLocations.entrySet()) {
                        Long conditionalPos = entry.getKey();
                        String conditionalType = entry.getValue();
                        if(conditionalType.equalsIgnoreCase("then")) {
                            randomAccessFile.seek(conditionalPos);
                            randomAccessFile.write(String.valueOf(line).getBytes());
                            randomAccessFile.seek(randomAccessFile.length());
                        }
                        else {
                            randomAccessFile.seek(conditionalPos);
                            randomAccessFile.write(String.valueOf(line + 1).getBytes());
                            randomAccessFile.seek(randomAccessFile.length());
                        }
                    }
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
        Iterator<Entry<IExpr, IStmt>> statementIterator = statement.iterator();
        int initialLine = this.line;
        while (statementIterator.hasNext()) {
            Entry<IExpr, IStmt> guardedStatement = statementIterator.next();
            guardedStatement.getKey().accept(this);
            guardedStatement.getValue().accept(this);
        }
        try {
            randomAccessFile.write((line++ + " GOTO " + initialLine + "\n\n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void visitRepeatStatement(final RepeatStmt statement) {
        try {
            randomAccessFile.write((line++ + " Repeat Unimplemented \n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void visitReturnStatement(final ReturnStmt statement) {
        try {
            randomAccessFile.write((line++ + " Return Unimplemented \n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        try {
            stackTop = expression.getOperator();
            expression.getLeft().accept(this);
            if(expression.getOperator() == Token.TokenType.OR) {
                randomAccessFile.write((line++ + " GOTO ").getBytes());
                gotoLocations.put(randomAccessFile.getFilePointer(), "then");
                randomAccessFile.write(("  \n").getBytes());
            }
            else {
                randomAccessFile.write((line++ + " GOTO ").getBytes());
                gotoLocations.put(randomAccessFile.getFilePointer(), "end");
                randomAccessFile.write(("  \n").getBytes());
            }
            expression.getRight().accept(this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void visitComparisonExpression(ComparisonExpr expression) {
        try {
            Enum<?> operator = expression.getOperator();
            if(operator == Token.TokenType.EQL){
                //MOVFW 0 RightAddress
                expression.getRight().accept(this);
                randomAccessFile.write((line++ + " MOVFW 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //SUBWF 0 LeftAddress
                expression.getLeft().accept(this);
                randomAccessFile.write((line++ + " SUBWF 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //BTFSS 2 3
                randomAccessFile.write((line++ + " BTFSS 2 3" + "\n").getBytes());
                stackTop = true;
            }
            else if(operator == Token.TokenType.NEQ){
                //MOVFW 0 RightAddress
                expression.getRight().accept(this);
                randomAccessFile.write((line++ + " MOVFW 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //SUBWF 0 LeftAddress
                expression.getLeft().accept(this);
                randomAccessFile.write((line++ + " SUBWF 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //BTFSC 2 3
                randomAccessFile.write((line++ + " BTFSC 2 3" + "\n").getBytes());
                stackTop = true;
            }
            else if(operator == Token.TokenType.GTR){
                //MOVFW 0 LeftAddress
                expression.getLeft().accept(this);
                randomAccessFile.write((line++ + " MOVFW 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //SUBWF 0 RightAddress
                expression.getRight().accept(this);
                randomAccessFile.write((line++ + " SUBWF 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //BTFSC 0 3
                randomAccessFile.write((line++ + " BTFSC 0 3" + "\n").getBytes());
                stackTop = true;
            }
            else if(operator == Token.TokenType.GEQ){
                //MOVFW 0 RightAddress
                expression.getRight().accept(this);
                randomAccessFile.write((line++ + " MOVFW 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //SUBWF 0 LeftAddress
                expression.getLeft().accept(this);
                randomAccessFile.write((line++ + " SUBWF 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //BTFSS 0 3
                randomAccessFile.write((line++ + " BTFSS 0 3" + "\n").getBytes());
                stackTop = true;
            }
            else if(operator == Token.TokenType.LSS){
                //MOVFW 0 RightAddress
                expression.getRight().accept(this);
                randomAccessFile.write((line++ + " MOVFW 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //SUBWF 0 LeftAddress
                expression.getLeft().accept(this);
                randomAccessFile.write((line++ + " SUBWF 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //BTFSS 0 3
                randomAccessFile.write((line++ + " BTFSS 0 3" + "\n").getBytes());
                stackTop = true;
            }
            else if(operator == Token.TokenType.LEQ){
                expression.getRight().accept(this);
                //MOVFW 0 RightAddress
                randomAccessFile.write((line++ + " MOVFW 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //SUBWF 0 LeftAddress
                expression.getLeft().accept(this);
                randomAccessFile.write((line++ + " SUBWF 0 " + ((Environment.EntryInfo)stackTop).value + "\n").getBytes());
                //BTFSC 0 3
                randomAccessFile.write((line++ + " BTFSC 0 3" + "\n").getBytes());
                stackTop = true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        try {
            randomAccessFile.write((line++ + " Unary Unimplemented \n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void visitCallExpression(final CallExpr expression) {
        try {
            randomAccessFile.write((line++ + " Call Unimplemented \n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void visitGetExpression(final GetExpr expression) {
        try {
            randomAccessFile.write((line++ + " Get Unimplemented \n").getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
