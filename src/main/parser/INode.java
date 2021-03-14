package main.parser;

/**
 * The interface Node.
 */
public interface INode {
    /**
     * Accept.
     *
     * @param visitor the visitor
     */
    void accept(IVisitor visitor);

}
