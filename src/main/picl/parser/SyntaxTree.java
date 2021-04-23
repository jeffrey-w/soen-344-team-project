package main.picl.parser;

import main.picl.interpreter.parser.ISyntaxTree;
import main.picl.interpreter.INode;

/**
 * The {@code SyntaxTree} provides an concrete implementation of the {@code ISyntaxTree} interface. It represents source
 * files written in the PICL programming language created by Niklaus Wirth.
 */
public class SyntaxTree implements ISyntaxTree<INode> {

    private final INode head;

    /**
     * Instantiates a new Syntax tree.
     *
     * @param head the top-level {@code IDecl} of this {@code SyntaxTree}
     */
    public SyntaxTree(INode head) {
        this.head = head;
    }

    @Override
    public INode getHead() {
        return this.head;
    }

}
