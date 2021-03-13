package main.picl.interpreter.decl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.picl.interpreter.stmt.IStmt;
import main.scanner.IToken;

public final class ModuleDecl implements IDecl {

    private final String identifier;
    private final List<IDecl> declarations;
    private IStmt statements;

    public ModuleDecl(IToken identifier) {
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

    public List<IDecl> getDeclarations() {
        return declarations;
    }

    public void addDeclaration(IDecl declaration) {
        declarations.add(Objects.requireNonNull(declaration));
    }

    public boolean hasStatements() {
        return statements != null;
    }

    public IStmt getStatements() {
        return statements;
    }

    public void addStatements(IStmt statements) {
        this.statements = Objects.requireNonNull(statements);
    }


    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitModuleDeclaration(this);
    }

}
