package main.picl.interpreter.decl;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.picl.interpreter.stmt.IStmt;
import main.scanner.IToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ProcedureDecl implements IDecl {

    private final String identifier;
    private final List<IDecl> declarations;
    private IDecl parameter;
    private Enum<?> returnType;
    private IStmt statements;
    private IStmt returnStatement;

    public ProcedureDecl(IToken identifier) {
        try {
            this.identifier = (String) identifier.getValue();
        } catch (ClassCastException e) {
            throw new Error(); // TODO need picl error
        }
        this.declarations = new ArrayList<>();
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void interpret(Environment environment) {
        System.out.print("PROCED " + identifier);
        if (parameter != null) {
            parameter.interpret(environment);
        }
        if (returnType != null) {
            System.out.print(": " + returnType);
        }
        System.out.println();
        for (IDecl declaration : declarations) {
            System.out.print("\t");
            declaration.interpret(environment);
        }
        System.out.print("BEGIN\n");
        statements.interpret(environment); // TODO can be null
        if (returnStatement != null) {
            returnStatement.interpret(environment);
        }
        System.out.print("END " + identifier + ".");
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitProcedureDeclaration(this);
    }

    public boolean hasParameter() {
        return parameter != null;
    }

    public IDecl getParameter() {
        return parameter;
    }

    public boolean hasReturnType() {
        return returnType != null;
    }

    public Enum<?> getReturnType() {
        return returnType;
    }

    public List<IDecl> getDeclarations() {
        return declarations;
    }

    public boolean hasStatements() {
        return statements != null;
    }

    public IStmt getStatements() {
        return statements;
    }

    public boolean hasReturnStatement() {
        return returnStatement != null;
    }

    public IStmt getReturnStatement() {
        return returnStatement;
    }

    public void addParameter(ParameterDecl parameter) {
        this.parameter = Objects.requireNonNull(parameter);
    }

    public void addReturnType(IToken previous) {
        this.returnType = previous.getType();
    }

    public void addDeclaration(IDecl declaration) {
        declarations.add(Objects.requireNonNull(declaration));
    }

    public void addStatements(IStmt statements) {
        this.statements = Objects.requireNonNull(statements);
    }

    public void addReturnStatement(IStmt returnStatement) {
        this.returnStatement = Objects.requireNonNull(returnStatement);
    }
}