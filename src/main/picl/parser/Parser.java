package main.picl.parser;

import main.parser.IParser;
import main.parser.ISyntaxTree;
import main.picl.interpreter.INode;
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
public class Parser implements IParser<INode> {

    private final IScanner scanner;
    private IToken previous, current;

    /**
     * Instantiates a new {@code Parser} with the specified {@code source} file.
     *
     * @param source the file to be parsed
     */
    public Parser(String source) {
        scanner = new Scanner(source); // TODO use factory?
        current = scanner.getToken();
    }

    @Override
    public ISyntaxTree<INode> parse() {
        return new SyntaxTree(moduleDeclaration()); // TODO check for EOF
    }

    private IDecl moduleDeclaration() {
        consume("Expect MODULE declaration.", MODULE);
        consume("Expect identifier after MODULE keyword.", IDENTIFIER);
        ModuleDecl module = new ModuleDecl(previous);
        consume("Expect ';' after MODULE identifier", SEMICOLON);
        if (match(CONST)) {
            module.addDeclaration(constantDeclaration());
        }
        while (match(INT, SET, BOOL)) {
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
        ensureIdentifiersEqual(module.getIdentifier());
        consume("Expect '.' after MODULE declaration.", PERIOD);
        return module;
    }

    private IDecl constantDeclaration() {
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

    private IDecl variableDeclaration() {
        VariableDecl variables = new VariableDecl(previous);
        do {
            consume("Expect identifier after variable type keyword.", IDENTIFIER);
            variables.add(previous, null);
        } while (match(COMMA));
        consume("Expect ';' after variable declaration.", SEMICOLON);
        return variables;
    }

    private IDecl procedureDeclaration() {
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
        consume("Expect ';' after procedure header.", SEMICOLON);
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
        ensureIdentifiersEqual(procedure.getIdentifier());
        consume("Expect ';' after PROCED declaration", SEMICOLON);
        return procedure;
    }

    private void ensureIdentifiersEqual(String identifier) {
        if (!identifier.equals(previous.getValue())) {
            // TODO throw error
        }
    }


    private IStmt statementSequence() {
        List<IStmt> statements = new ArrayList<>();
        do {
            statements.add(statement());
        } while (match(SEMICOLON) && !(match(END) || current.getType() == RETURN));
        return new BlockStmt(statements);
    }

    private IStmt statement() {
        if (match(IF)) {
            return ifStatement();
        } else if (match(WHILE)) {
            return whileStatement();
        } else if (match(REPEAT)) {
            return repeatStmt();
        }
        return expressionStatement();
    }

    private IStmt ifStatement() {
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

    private IStmt whileStatement() {
        WhileStmt statement = new WhileStmt();
        IExpr guard = disjunction();
        consume("Expect DO after condition in WHILE statement.", DO);
        statement.addStatement(guard, statementSequence());
        while (match(ELSIF)) {
            guard = disjunction();
            consume("Expect DO after condition in ELSIF statement.", DO);
            statement.addStatement(guard, statementSequence());
        }
        consume("Expect END after WHILE statement.", END);
        return statement;
    }

    private IStmt repeatStmt() {
        IStmt statements = statementSequence();
        IExpr condition = null;
        if (match(UNTIL)) {
            condition = disjunction();
        } else {
            consume("Expect END after REPEAT statement without UNTIL condition.", END);
        }
        return new RepeatStmt(condition, statements);
    }

    private IStmt returnStatement() {
        return new ReturnStmt(assignment());
    }


    private IStmt expressionStatement() {
        return new ExpressionStmt(assignment());
    }

    private IExpr assignment() {
        IExpr left = disjunction();
        if (match(BECOMES)) {
            IToken operator = previous;
            IExpr right = disjunction();
            return new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private IExpr disjunction() {
        IExpr left = conjunction();
        while (match(OR)) {
            IToken operator = previous;
            IExpr right = conjunction();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private IExpr conjunction() {
        IExpr left = equality();
        while (match(AND)) {
            IToken operator = previous;
            IExpr right = equality();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private IExpr equality() {
        IExpr left = comparison();
        while (match(EQL, NEQ)) {
            IToken operator = previous;
            IExpr right = comparison();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private IExpr comparison() {
        IExpr left = addition();
        while (match(GTR, GEQ, LSS, LEQ)) {
            IToken operator = previous;
            IExpr right = addition();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private IExpr addition() {
        IExpr left = multiplication();
        while (match(PLUS, MINUS)) {
            IToken operator = previous;
            IExpr right = multiplication();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private IExpr multiplication() {
        IExpr left = unary();
        while (match(AST, SLASH)) {
            IToken operator = previous;
            IExpr right = unary();
            left = new BinaryExpr(left, right, operator);
        }
        return left;
    }

    private IExpr unary() {
        if (match(QUERY, OP, NOT, INC, DEC, ROR, ROL)) {
            IToken operator = previous;
            IExpr operand = unary();
            return new UnaryExpr(operator, operand);
        }
        return call();
    }

    private IExpr call() {
        IExpr left = primary();
        if (match(LEFT_PARENTHESIS)) {
            IExpr argument = assignment();
            consume("Expect ')' after argument list in procedure call.", RIGHT_PARENTHESIS);
            left = new CallExpr(left, argument);
        } else if (match(PERIOD)) {
            IExpr index = primary();
            left = new GetExpr(left, index);
        }
        return left;
    }

    private IExpr primary() {
        if (match(IDENTIFIER)) {
            return new VariableExpr(previous);
        } else if (match(NUMBER)) {
            return new LiteralExpr(previous);
        }
        throw new Error(); // TODO need picl error
    }

    private void consume(String message, Token.TokenType... types) {
        for (Token.TokenType type : types) {
            if (current.getType() == type) {
                advance();
                return;
            }
        }
        // TODO throw error with message
    }

    private boolean match(Token.TokenType... types) {
        for (Token.TokenType type : types) {
            if (current.getType() == type) {
                advance();
                return true;
            }
        }
        return false;
    }

    private void advance() {
        previous = current;
        current = scanner.getToken();
    }

}
