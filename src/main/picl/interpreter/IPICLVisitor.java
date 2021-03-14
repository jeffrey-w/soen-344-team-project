package main.picl.interpreter;

import main.parser.IVisitor;
import main.picl.interpreter.decl.ModuleDecl;
import main.picl.interpreter.decl.ParameterDecl;
import main.picl.interpreter.decl.ProcedureDecl;
import main.picl.interpreter.decl.VariableDecl;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;

/**
 * The interface Ipicl visitor.
 */
public interface IPICLVisitor extends IVisitor{

    /**
     * Visit module declaration.
     *
     * @param declaration the declaration
     */
    void visitModuleDeclaration(ModuleDecl declaration);

    /**
     * Visit variable declaration.
     *
     * @param declaration the declaration
     */
    void visitVariableDeclaration(VariableDecl declaration);

    /**
     * Visit procedure declaration.
     *
     * @param declaration the declaration
     */
    void visitProcedureDeclaration(ProcedureDecl declaration);

    /**
     * Visit parameter declaration.
     *
     * @param declaration the declaration
     */
    void visitParameterDeclaration(ParameterDecl declaration);

    /**
     * Visit block statement.
     *
     * @param statement the statement
     */
    void visitBlockStatement(BlockStmt statement);

    /**
     * Visit if statement.
     *
     * @param statement the statement
     */
    void visitIfStatement(IfStmt statement);

    /**
     * Visit while statement.
     *
     * @param statement the statement
     */
    void visitWhileStatement(WhileStmt statement);

    /**
     * Visit repeat statement.
     *
     * @param statement the statement
     */
    void visitRepeatStatement(RepeatStmt statement);

    /**
     * Visit return statement.
     *
     * @param statement the statement
     */
    void visitReturnStatement(ReturnStmt statement);

    /**
     * Visit expression statement.
     *
     * @param statement the statement
     */
    void visitExpressionStatement(ExpressionStmt statement);

    /**
     * Visit binary expression.
     *
     * @param expression the expression
     */
    void visitBinaryExpression(BinaryExpr expression);

    /**
     * Visit unary expression.
     *
     * @param expression the expression
     */
    void visitUnaryExpression(UnaryExpr expression);

    /**
     * Visit call expression.
     *
     * @param expression the expression
     */
    void visitCallExpression(CallExpr expression);

    /**
     * Visit get expression.
     *
     * @param expression the expression
     */
    void visitGetExpression(GetExpr expression);

    /**
     * Visit variable expression.
     *
     * @param expression the expression
     */
    void visitVariableExpression(VariableExpr expression);

    /**
     * Visit literal expression.
     *
     * @param expression the expression
     */
    void visitLiteralExpression(LiteralExpr expression);

}
