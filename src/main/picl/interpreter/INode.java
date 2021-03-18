package main.picl.interpreter;

/**
 * The {@code INode} interface specifies operations on an interpretable representation of the PICL language.
 */
public interface INode { // TODO capture position information

    /**
     * Allows the specified {@code visitor} to interpret this {@code INode}.
     *
     * @param visitor the specified {@code IVisitor}
     */
    void accept(IVisitor visitor);

}
