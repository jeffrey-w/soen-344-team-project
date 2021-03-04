package main.picl.interpreter.decl;

import main.picl.interpreter.Environment;
import main.picl.interpreter.stmt.IStmt;
import main.scanner.IToken;

import java.util.List;

public final class ModuleDecl implements IDecl {

    private final String identifier;
    private final List<IDecl> declarations;
    private final IStmt statements;

    public ModuleDecl(IToken identifier, List<IDecl> declarations, IStmt statements) {
        this.identifier = (String) identifier.getValue();
        this.declarations = declarations;
        this.statements = statements;
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

}
