package main.picl.interpreter.decl;

import main.picl.interpreter.Environment;
import main.picl.interpreter.IVisitor;
import main.scanner.IToken;

public final class ParameterDecl implements IDecl {

    private final Enum<?> type;
    private final String identifier;

    public ParameterDecl(IToken token, IToken identifier) {
        type = token.getType(); // TODO validate token
        this.identifier = (String) identifier.getValue(); // TODO catch class cast exception
    }

    @Override
    public void interpret(Environment environment) {
        System.out.print("(" + type + " " + identifier + ")");
    }

    @Override
    public void accept(final IVisitor visitor) {
        visitor.visitParameterDeclaration(this);
    }

    public Enum<?> getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

}
