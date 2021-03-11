package main.picl.interpreter;

import main.picl.interpreter.decl.ModuleDecl;
import main.picl.interpreter.decl.ParameterDecl;
import main.picl.interpreter.decl.ProcedureDecl;
import main.picl.interpreter.decl.VariableDecl;
import main.picl.interpreter.expr.*;
import main.picl.interpreter.stmt.*;

public interface IVisitor {

    void visitModuleDeclaration(ModuleDecl declaration);
    void visitVariableDeclaration(VariableDecl declaration);
    void visitProcedureDeclaration(ProcedureDecl declaration);
    void visitParameterDeclaration(ParameterDecl declaration);
    void visitBlockStatement(BlockStmt statement);
    void visitIfStatement(IfStmt statement);
    void visitWhileStatement(WhileStmt statement);
    void visitRepeatStatement(RepeatStmt statement);
    void visitReturnStatement(ReturnStmt statement);
    void visitExpressionStatement(ExpressionStmt statement);
    void visitBinaryExpression(BinaryExpr expression);
    void visitUnaryExpression(UnaryExpr expression);
    void visitCallExpression(CallExpr expression);
    void visitGetExpression(GetExpr expression);
    void visitVariableExpression(VariableExpr expression);
    void visitLiteralExpression(LiteralExpr expression);

}
