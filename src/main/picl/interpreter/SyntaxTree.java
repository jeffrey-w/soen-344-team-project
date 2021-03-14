package main.picl.interpreter;

import main.picl.interpreter.decl.ModuleDecl;

/**
 * The type Syntax tree.
 */
public class SyntaxTree {

    /**
     * The Head.
     */
    ModuleDecl head;

    /**
     * Instantiates a new Syntax tree.
     *
     * @param head the head
     */
    public SyntaxTree(ModuleDecl head){
        this.head = head;
    }

    /**
     * Gets head.
     *
     * @return the head
     */
    public ModuleDecl getHead() {
        return this.head;
    }

}
