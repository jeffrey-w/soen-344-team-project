package main.picl.interpreter;

import main.picl.interpreter.decl.ModuleDecl;

public class SyntaxTree {

    ModuleDecl head;

    public SyntaxTree(ModuleDecl head){
        this.head = head;
    }

    public ModuleDecl getHead() {
        return this.head;
    }

}
