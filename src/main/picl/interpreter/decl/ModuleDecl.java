package main.picl.interpreter.decl;

import main.picl.interpreter.IVisitor;
import main.picl.interpreter.stmt.IStmt;
import main.scanner.IToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Module decl.
 */
public final class ModuleDecl implements IDecl {

    private final String identifier;
    private final List<IDecl> declarations;
    private IStmt statements;

    /**
     * Instantiates a new Module decl.
     *
     * @param identifier the identifier
     */
    public ModuleDecl(IToken identifier) {
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

    /**
     * Gets declarations.
     *
     * @return the declarations
     */
    public List<IDecl> getDeclarations() {
        return declarations;
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
     * Add statements.
     *
     * @param statements the statements
     */
    public void addStatements(IStmt statements) {
        this.statements = Objects.requireNonNull(statements);
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitModuleDeclaration(this);
    }

}
