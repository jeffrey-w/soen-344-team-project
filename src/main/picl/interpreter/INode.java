package main.picl.interpreter;

public interface INode {

    void interpret(Environment environment);
    void accept(IVisitor visitor);

}
