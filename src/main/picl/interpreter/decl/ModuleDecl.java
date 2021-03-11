package main.picl.interpreter.decl;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.picl.interpreter.stmt.IStmt;
import main.scanner.IToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public void interpret(Environment environment) {
        System.out.print("MODULE " + identifier + ";\n");
        for (IDecl declaration : declarations) {
            System.out.print("\t");
            declaration.interpret(environment);
        }
        System.out.print("BEGIN\n");
        statements.interpret(environment); // TODO can be null
        System.out.print("END " + identifier + ".");
    }

    @Override
    public void accept(final IVisitor visitor) {
        visitor.visitModuleDeclaration(this);
    }

}
