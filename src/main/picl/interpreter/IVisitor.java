package main.picl.interpreter;

import main.picl.interpreter.decl.ModuleDecl;
import main.picl.interpreter.decl.ParameterDecl;
import main.picl.interpreter.decl.ProcedureDecl;
import main.picl.interpreter.decl.VariableDecl;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;

/**
 * The {@code IVisitor} interface specified operations on an interpreter of {@code INodes}.
 */
public interface IVisitor {

    /**
     * Interprets the specified {@code declaration}
     *
     * @param declaration specified {@code ModuleDecl}
     */
    void visitModuleDeclaration(ModuleDecl declaration);

    /**
     * Interprets the specified {@code declaration}
     *
     * @param declaration specified {@code VariableDecl}
     */
    void visitVariableDeclaration(VariableDecl declaration);

    /**
     * Interprets the specified {@code declaration}
     *
     * @param declaration specified {@code ProcedureDecl}
     */
    void visitProcedureDeclaration(ProcedureDecl declaration);

    /**
     * Interprets the specified {@code declaration}
     *
     * @param declaration specified {@code ParameterDecl}
     */
    void visitParameterDeclaration(ParameterDecl declaration);

    /**
     * Interprets the specified {@code statement}
     *
     * @param statement specified {@code BlockStmt}
     */
    void visitBlockStatement(BlockStmt statement);

    /**
     * Interprets the specified {@code statement}
     *
     * @param statement specified {@code IfStmt}
     */
    void visitIfStatement(IfStmt statement);

    /**
     * Interprets the specified {@code statement}
     *
     * @param statement specified {@code WhileStmt}
     */
    void visitWhileStatement(WhileStmt statement);

    /**
     * Interprets the specified {@code statement}
     *
     * @param statement specified {@code RepeatStmt}
     */
    void visitRepeatStatement(RepeatStmt statement);

    /**
     * Interprets the specified {@code statement}
     *
     * @param statement specified {@code ReturnStmt}
     */
    void visitReturnStatement(ReturnStmt statement);

    /**
     * Interprets the specified {@code statement}
     *
     * @param statement specified {@code ExpressionStmt}
     */
    void visitExpressionStatement(ExpressionStmt statement);

    void visitAssignmentExpression(AssignmentExpr expression);

    void visitLogicalExpression(LogicalExpr expression);

    void visitComparisonExpression(ComparisonExpr expression);

    void visitArithmeticExpression(ArithmeticExpr expression);

    /**
     * Interprets the specified {@code expression}
     *
     * @param expression specified {@code UnaryExpr}
     */
    void visitUnaryExpression(UnaryExpr expression);

    /**
     * Interprets the specified {@code expression}
     *
     * @param expression specified {@code CallExpr}
     */
    void visitCallExpression(CallExpr expression);

    /**
     * Interprets the specified {@code expression}
     *
     * @param expression specified {@code GetExpr}
     */
    void visitGetExpression(GetExpr expression);

    /**
     * Interprets the specified {@code expression}
     *
     * @param expression specified {@code VariableExpr}
     */
    void visitVariableExpression(VariableExpr expression);

    /**
     * Interprets the specified {@code expression}
     *
     * @param expression specified {@code LiteralExpr}
     */
    void visitLiteralExpression(LiteralExpr expression);

}
