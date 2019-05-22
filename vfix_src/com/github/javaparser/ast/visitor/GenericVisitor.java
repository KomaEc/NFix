package com.github.javaparser.ast.visitor;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsStmt;
import com.github.javaparser.ast.modules.ModuleOpensStmt;
import com.github.javaparser.ast.modules.ModuleProvidesStmt;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.modules.ModuleUsesStmt;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;

public interface GenericVisitor<R, A> {
   R visit(CompilationUnit n, A arg);

   R visit(PackageDeclaration n, A arg);

   R visit(TypeParameter n, A arg);

   R visit(LineComment n, A arg);

   R visit(BlockComment n, A arg);

   R visit(ClassOrInterfaceDeclaration n, A arg);

   R visit(EnumDeclaration n, A arg);

   R visit(EnumConstantDeclaration n, A arg);

   R visit(AnnotationDeclaration n, A arg);

   R visit(AnnotationMemberDeclaration n, A arg);

   R visit(FieldDeclaration n, A arg);

   R visit(VariableDeclarator n, A arg);

   R visit(ConstructorDeclaration n, A arg);

   R visit(MethodDeclaration n, A arg);

   R visit(Parameter n, A arg);

   R visit(InitializerDeclaration n, A arg);

   R visit(JavadocComment n, A arg);

   R visit(ClassOrInterfaceType n, A arg);

   R visit(PrimitiveType n, A arg);

   R visit(ArrayType n, A arg);

   R visit(ArrayCreationLevel n, A arg);

   R visit(IntersectionType n, A arg);

   R visit(UnionType n, A arg);

   R visit(VoidType n, A arg);

   R visit(WildcardType n, A arg);

   R visit(UnknownType n, A arg);

   R visit(ArrayAccessExpr n, A arg);

   R visit(ArrayCreationExpr n, A arg);

   R visit(ArrayInitializerExpr n, A arg);

   R visit(AssignExpr n, A arg);

   R visit(BinaryExpr n, A arg);

   R visit(CastExpr n, A arg);

   R visit(ClassExpr n, A arg);

   R visit(ConditionalExpr n, A arg);

   R visit(EnclosedExpr n, A arg);

   R visit(FieldAccessExpr n, A arg);

   R visit(InstanceOfExpr n, A arg);

   R visit(StringLiteralExpr n, A arg);

   R visit(IntegerLiteralExpr n, A arg);

   R visit(LongLiteralExpr n, A arg);

   R visit(CharLiteralExpr n, A arg);

   R visit(DoubleLiteralExpr n, A arg);

   R visit(BooleanLiteralExpr n, A arg);

   R visit(NullLiteralExpr n, A arg);

   R visit(MethodCallExpr n, A arg);

   R visit(NameExpr n, A arg);

   R visit(ObjectCreationExpr n, A arg);

   R visit(ThisExpr n, A arg);

   R visit(SuperExpr n, A arg);

   R visit(UnaryExpr n, A arg);

   R visit(VariableDeclarationExpr n, A arg);

   R visit(MarkerAnnotationExpr n, A arg);

   R visit(SingleMemberAnnotationExpr n, A arg);

   R visit(NormalAnnotationExpr n, A arg);

   R visit(MemberValuePair n, A arg);

   R visit(ExplicitConstructorInvocationStmt n, A arg);

   R visit(LocalClassDeclarationStmt n, A arg);

   R visit(AssertStmt n, A arg);

   R visit(BlockStmt n, A arg);

   R visit(LabeledStmt n, A arg);

   R visit(EmptyStmt n, A arg);

   R visit(ExpressionStmt n, A arg);

   R visit(SwitchStmt n, A arg);

   R visit(SwitchEntryStmt n, A arg);

   R visit(BreakStmt n, A arg);

   R visit(ReturnStmt n, A arg);

   R visit(IfStmt n, A arg);

   R visit(WhileStmt n, A arg);

   R visit(ContinueStmt n, A arg);

   R visit(DoStmt n, A arg);

   R visit(ForeachStmt n, A arg);

   R visit(ForStmt n, A arg);

   R visit(ThrowStmt n, A arg);

   R visit(SynchronizedStmt n, A arg);

   R visit(TryStmt n, A arg);

   R visit(CatchClause n, A arg);

   R visit(LambdaExpr n, A arg);

   R visit(MethodReferenceExpr n, A arg);

   R visit(TypeExpr n, A arg);

   R visit(NodeList n, A arg);

   R visit(Name n, A arg);

   R visit(SimpleName n, A arg);

   R visit(ImportDeclaration n, A arg);

   R visit(ModuleDeclaration n, A arg);

   R visit(ModuleRequiresStmt n, A arg);

   R visit(ModuleExportsStmt n, A arg);

   R visit(ModuleProvidesStmt n, A arg);

   R visit(ModuleUsesStmt n, A arg);

   R visit(ModuleOpensStmt n, A arg);

   R visit(UnparsableStmt n, A arg);

   R visit(ReceiverParameter n, A arg);

   R visit(VarType n, A arg);
}
