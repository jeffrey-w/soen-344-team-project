package main.picl.interpreter.decl;

import main.picl.interpreter.Environment;
import main.scanner.IToken;

public final class FormalParameterDecl implements IDecl {

    private final Enum<?> type;
    private final String identifier;

    public FormalParameterDecl(IToken token, IToken identifier) {
        type = token.getType(); // TODO validate token
        this.identifier = (String) identifier.getValue(); // TODO catch class cast exception
    }

    @Override
    public void interpret(Environment environment) {
        System.out.print("(" + type + " " + identifier + ")");
    }

}
