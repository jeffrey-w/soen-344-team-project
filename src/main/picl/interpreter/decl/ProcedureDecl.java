package main.picl.interpreter.decl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.picl.interpreter.stmt.IStmt;
import main.scanner.IToken;

/**
 * The type Procedure decl.
 */
public final class ProcedureDecl implements IDecl {

    private final String identifier;
    private final List<IDecl> declarations;
    private IDecl parameter;
    private Enum<?> returnType;
    private IStmt statements;
    private IStmt returnStatement;

    /**
     * Instantiates a new Procedure decl.
     *
     * @param identifier the identifier
     */
    public ProcedureDecl(IToken identifier) {
        try {
            this.identifier = (String) identifier.getValue();
        } catch (ClassCastException e) {
            throw new Error(); // TODO need picl error
        }
        this.declarations = new ArrayList<>();
    }

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void accept(IVisitor visitor) {
        ((IPICLVisitor) visitor).visitProcedureDeclaration(this);
    }

    /**
     * Has parameter boolean.
     *
     * @return the boolean
     */
    public boolean hasParameter() {
        return parameter != null;
    }

    /**
     * Gets parameter.
     *
     * @return the parameter
     */
    public IDecl getParameter() {
        return parameter;
    }

    /**
     * Has return type boolean.
     *
     * @return the boolean
     */
    public boolean hasReturnType() {
        return returnType != null;
    }

    /**
     * Gets return type.
     *
     * @return the return type
     */
    public Enum<?> getReturnType() {
        return returnType;
    }

    /**
     * Gets declarations.
     *
     * @return the declarations
     */
    public List<IDecl> getDeclarations() {
        return declarations;
    }

    /**
     * Has statements boolean.
     *
     * @return the boolean
     */
    public boolean hasStatements() {
        return statements != null;
    }

    /**
     * Gets statements.
     *
     * @return the statements
     */
    public IStmt getStatements() {
        return statements;
    }

    /**
     * Has return statement boolean.
     *
     * @return the boolean
     */
    public boolean hasReturnStatement() {
        return returnStatement != null;
    }

    /**
     * Gets return statement.
     *
     * @return the return statement
     */
    public IStmt getReturnStatement() {
        return returnStatement;
    }

    /**
     * Add parameter.
     *
     * @param parameter the parameter
     */
    public void addParameter(ParameterDecl parameter) {
        this.parameter = Objects.requireNonNull(parameter);
    }

    /**
     * Add return type.
     *
     * @param previous the previous
     */
    public void addReturnType(IToken previous) {
        this.returnType = previous.getType();
    }

    /**
     * Add declaration.
     *
     * @param declaration the declaration
     */
    public void addDeclaration(IDecl declaration) {
        declarations.add(Objects.requireNonNull(declaration));
    }

    /**
     * Add statements.
     *
     * @param statements the statements
     */
    public void addStatements(IStmt statements) {
        this.statements = Objects.requireNonNull(statements);
    }

    /**
     * Add return statement.
     *
     * @param returnStatement the return statement
     */
    public void addReturnStatement(IStmt returnStatement) {
        this.returnStatement = Objects.requireNonNull(returnStatement);
    }
}