package main.picl.parser;

import main.picl.interpreter.SyntaxTree;
import main.picl.interpreter.decl.*;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;
import main.picl.scanner.Scanner;
import main.picl.scanner.Token;
import main.scanner.IScanner;
import main.scanner.IToken;

import java.util.ArrayList;
import java.util.List;

import static main.picl.scanner.Token.TokenType.*;

/**
 * The type Parser.
 */
public class Parser {

    private final IScanner scanner;
    private IToken previous, current;

    /**
     * Instantiates a new Parser.
     *
     * @param source the source
     */
    public Parser(String source) {
        scanner = new Scanner(source); // TODO use factory?
        current = scanner.getToken();
    }

    /**
     * Parse syntax tree.
     *
     * @return the syntax tree
     */
    public SyntaxTree parse() {
        return new SyntaxTree((ModuleDecl) moduleDeclaration()); // TODO check for EOF
    }

    /**
     * Module declaration decl.
     *
     * @return the decl
     */
    IDecl moduleDeclaration() {
        consume("Expect MODULE declaration.", MODULE);
        consume("Expect identifier after MODULE keyword.", IDENTIFIER);
        ModuleDecl module = new ModuleDecl(previous);
        consume("Expect ';' after MODULE identifier", SEMICOLON);
        if (match(CONST)) {
            module.addDeclaration(constantDeclaration());
        }
        if (match(INT, SET, BOOL)) {
            module.addDeclaration(variableDeclaration());
        }
        while (match(PROCEDURE)) {
            module.addDeclaration(procedureDeclaration());
        }
        if (match(BEGIN)) {
            module.addStatements(statementSequence());
        }
        consume("Expect END keyword after MODULE body.", END);
        consume("Expect identifier after END keyword in MODULE declaration.", IDENTIFIER);
        if (!module.getIdentifier().equals(previous.getValue())) {
            // TODO throw error
        }
        consume("Expect '.' after MODULE declaration.", PERIOD);
        return module;
    }

    /**
     * Constant declaration decl.
     *
     * @return the decl
     */
    IDecl constantDeclaration() {
        VariableDecl constants = new VariableDecl(null);
        do {
            consume("Expect identifier after CONST keyword.", IDENTIFIER);
            IToken identifier = previous;
            consume("Expect '=' after CONST identifier.", EQL);
            consume("Expect value after '=' in CONST declaration.", NUMBER);
            IToken value = previous;
            constants.add(identifier, value);
        } while (match(COMMA));
        consume("Expect ';' after CONST declaration.", SEMICOLON);
        return constants;
    }

    /**
     * Variable declaration decl.
     *
     * @return the decl
     */
    IDecl variableDeclaration() {
        VariableDecl variables = new VariableDecl(previous);
        do {
            consume("Expect identifier after variable type keyword.", IDENTIFIER);
            variables.add(previous, null);
        } while (match(COMMA));
        consume("Expect ';' after variable declaration.", SEMICOLON);
        return variables;
    }

    /**
     * Procedure declaration decl.
     *
     * @return the decl
     */
    IDecl procedureDeclaration() {
        consume("Expect identifier after PROCED keyword.", IDENTIFIER);
        ProcedureDecl procedure = new ProcedureDecl(previous);
        if (match(LEFT_PARENTHESIS)) {
            consume("Expect variable type after '(' in formal parameter list.", INT, SET, BOOL);
            IToken type = previous;
            consume("Expect identifier after variable type in formal parameter list.", IDENTIFIER);
            procedure.addParameter(new ParameterDecl(type, previous));
            consume("Expect ')' after formal parameter list.", RIGHT_PARENTHESIS);
        }
        if (match(COLON)) {
            consume("Expect variable type after ':' in procedure header.", INT, SET, BOOL);
            procedure.addReturnType(previous);
        }
        while (match(INT, SET, BOOL)) {
            procedure.addDeclaration(variableDeclaration());
        }
        if (match(BEGIN)) {
            procedure.addStatements(statementSequence());
        }
        if (match(RETURN)) {
            procedure.addReturnStatement(returnStatement());
        }
        consume("Expect END keyword after PROCED body.", END);
        consume("Expect identifier after END keyword in PROCED declaration.", IDENTIFIER);
        if (!procedure.getIdentifier().equals(previous.getValue())) {
            // TODO throw error
        }
        return procedure;
    }

    /**
     * Statement sequence stmt.
     *
     * @return the stmt
     */
    IStmt statementSequence() {
        List<IStmt> statements = new ArrayList<>();
        do {
            statements.add(statement());
        } while (match(SEMICOLON));
        return new BlockStmt(statements);
    }

    /**
     * Statement stmt.
     *
     * @return the stmt
     */
    IStmt statement() {
        if (match(IF)) {
            return ifStatement();
        } else if (match(WHILE)) {
            return whileStatement();
        } else if (match(REPEAT)) {
            return repeatStmt();
        }
        return expressionStatement();
    }

    /**
     * If statement stmt.
     *
     * @return the stmt
     */
    IStmt ifStatement() {
        IfStmt statement = new IfStmt();
        IExpr guard = disjunction();
        consume("Expect THEN after condition in IF statement.", THEN);
        statement.addStatement(guard, statementSequence());
        while (match(ELSIF)) {
            guard = disjunction();
            consume("Expect THEN after condition in ELSIF statement.", THEN);
            statement.addStatement(guard, statementSequence());
        }
        if (match(ELSE)) {
            statement.addElse(statementSequence());
        }
        consume("Expect END after IF statement.", END);
        return statement;
    }

    /**
     * While statement stmt.
     *
     * @return the stmt
     */
    IStmt whileStatement() {
        WhileStmt statement = new WhileStmt();
        IExpr guard = disjunction();
        consume("Expect DO after condition in WHILE statement.", DO);
        statement.addStatement(guard, statementSequence());
        while (match(ELSIF)) {
            guard = disjunction();
            consume("Expect DO after condition in ELSIF statement", DO);
            statement.addStatement(guard, statementSequence());
        }
        consume("Expect END after WHILE statement.", END);
        return statement;
    }

    /**
     * Repeat stmt stmt.
     *
     * @return the stmt
     */
    IStmt repeatStmt() {
        IStmt statements = statementSequence();
        IExpr condition = null;
        if (match(UNTIL)) {
            condition = disjunction();
        }
        consume("Expect END after REPEAT statement", END);
        return new RepeatStmt(condition, statements);
    }

    /**
     * Return statement stmt.
     *
     * @return the stmt
     */
    IStmt returnStatement() {
        return new ReturnStmt(assignment());
    }

    /**
     * Expression statement stmt.
     *
     * @return the stmt
     */
    IStmt expressionStatement() {
        return new ExpressionStmt(assignment());
    }

    /**
     * Assignment expr.
     *
     * @return the expr
     */
    IExpr assignment() {
        IExpr left = disjunction();
        if (match(BECOMES)) {
            IToken operator = previous;
            IExpr right = disjunction();
            return new BinaryExpr(left, right, operator);
        }
        return left;
    }

    /**
     * Disjunction expr.
     *
     * @return the expr
     */
    IExpr disjunction() {
        IExpr left = conjunction();
        while (match(OR)) {
            IToken operator = previous;
            IExpr right = conjunction();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    /**
     * Conjunction expr.
     *
     * @return the expr
     */
    IExpr conjunction() {
        IExpr left = equality();
        while (match(AND)) {
            IToken operator = previous;
            IExpr right = equality();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    /**
     * Equality expr.
     *
     * @return the expr
     */
    IExpr equality() {
        IExpr left = comparison();
        while (match(EQL, NEQ)) {
            IToken operator = previous;
            IExpr right = comparison();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    /**
     * Comparison expr.
     *
     * @return the expr
     */
    IExpr comparison() {
        IExpr left = addition();
        while (match(GTR, GEQ, LSS, LEQ)) {
            IToken operator = previous;
            IExpr right = addition();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    /**
     * Addition expr.
     *
     * @return the expr
     */
    IExpr addition() {
        IExpr left = multiplication();
        while (match(PLUS, MINUS)) {
            IToken operator = previous;
            IExpr right = multiplication();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    /**
     * Multiplication expr.
     *
     * @return the expr
     */
    IExpr multiplication() {
        IExpr left = unary();
        while (match(AST, SLASH)) {
            IToken operator = previous;
            IExpr right = unary();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    /**
     * Unary expr.
     *
     * @return the expr
     */
    IExpr unary() {
        if (match(OP, NOT, INC, DEC, ROR, ROL)) {
            IToken operator = previous;
            IExpr operand = unary();
            return new UnaryExpr(operator, operand);
        }
        return call();
    }

    /**
     * Call expr.
     *
     * @return the expr
     */
    IExpr call() {
        IExpr left = primary();
        if (match(LEFT_PARENTHESIS)) {
            IExpr argument = assignment();
            left = new CallExpr(left, argument);
        } else if (match(PERIOD)) {
            IExpr index = primary();
            left = new GetExpr(left, index);
        }
        return left;
    }

    /**
     * Primary expr.
     *
     * @return the expr
     */
    IExpr primary() {
        if (match(IDENTIFIER)) {
            return new VariableExpr(previous);
        } else if (match(NUMBER)) {
            return new LiteralExpr(previous);
        }
        throw new Error(); // TODO need picl error
    }

    /**
     * Consume.
     *
     * @param message the message
     * @param types   the types
     */
    void consume(String message, Token.TokenType... types) {
        for (Token.TokenType type : types) {
            if (current.getType() == type) {
                advance();
                return;
            }
        }
        // TODO throw error with message
    }

    /**
     * Match boolean.
     *
     * @param types the types
     * @return the boolean
     */
    boolean match(Token.TokenType... types) {
        for (Token.TokenType type : types) {
            if (current.getType() == type) {
                advance();
                return true;
            }
        }
        return false;
    }

    /**
     * Advance.
     */
    void advance() {
        previous = current;
        current = scanner.getToken();
    }

}
