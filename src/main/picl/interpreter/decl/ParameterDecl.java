package main.picl.interpreter.decl;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.scanner.IToken;

public final class ParameterDecl implements IDecl {

    private final Enum<?> type;
    private final String identifier;

    public ParameterDecl(IToken token, IToken identifier) {
        type = token.getType(); // TODO validate token
        this.identifier = (String) identifier.getValue(); // TODO catch class cast exception
    }

    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitParameterDeclaration(this);
    }

    public Enum<?> getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

}
