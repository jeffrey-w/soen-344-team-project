package main.picl.parser;

import main.parser.ISyntaxTree;
import main.picl.interpreter.decl.ModuleDecl;

/**
 * The {@code SyntaxTree} provides an concrete implementation of the {@code ISyntaxTree} interface. It represents source
 * files written in the PICL programming language created by Niklaus Wirth.
 */
public class SyntaxTree implements ISyntaxTree<ModuleDecl> {

    private final ModuleDecl head;

    /**
     * Instantiates a new Syntax tree.
     *
     * @param head the top-level {@code IDecl} of this {@code SyntaxTree}
     */
    public SyntaxTree(ModuleDecl head) {
        this.head = head;
    }

    @Override
    public ModuleDecl getHead() {
        return this.head;
    }

}
