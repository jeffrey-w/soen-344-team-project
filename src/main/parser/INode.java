package main.parser;

public interface INode {
    void accept(IVisitor visitor);

}
