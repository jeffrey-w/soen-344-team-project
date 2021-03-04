package main.picl.interpreter.decl;

import main.picl.interpreter.Environment;
import main.picl.interpreter.stmt.IStmt;
import main.scanner.IToken;

import java.util.List;

public final class ProcedureDecl implements IDecl {

    private final String identifier;
    private final IDecl parameter;
    private final IToken type;
    private final List<IDecl> declarations;
    private final IStmt statements;
    private final IStmt returnStatement;

    public ProcedureDecl(IToken identifier, IDecl parameter, IToken type, List<IDecl> declarations, IStmt statements,
            IStmt returnStatement) {
        this.identifier = (String) identifier.getValue();
        this.parameter = parameter;
        this.type = type;
        this.declarations = declarations;
        this.statements = statements;
        this.returnStatement = returnStatement;
    }

    @Override
    public void interpret(Environment environment) {
        System.out.print("PROCED " + identifier);
        if (parameter != null) {
            parameter.interpret(environment);
        }
        if (type != null) {
            System.out.print(": " + type);
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

}