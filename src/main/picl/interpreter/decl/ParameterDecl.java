package main.picl.interpreter.decl;

import main.parser.IVisitor;
import main.picl.interpreter.IPICLVisitor;
import main.scanner.IToken;

/**
 * The type Parameter decl.
 */
public final class ParameterDecl implements IDecl {

    private final Enum<?> type;
    private final String identifier;

    /**
     * Instantiates a new Parameter decl.
     *
     * @param token      the token
     * @param identifier the identifier
     */
    public ParameterDecl(IToken token, IToken identifier) {
        type = token.getType(); // TODO validate token
        this.identifier = (String) identifier.getValue(); // TODO catch class cast exception
    }

    @Override
    public void accept(final IVisitor visitor) {
        ((IPICLVisitor) visitor).visitParameterDeclaration(this);
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Enum<?> getType() {
        return type;
    }

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

}
