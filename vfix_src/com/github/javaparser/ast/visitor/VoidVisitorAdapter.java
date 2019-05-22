package com.github.javaparser.ast.visitor;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
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
import java.util.Iterator;

public abstract class VoidVisitorAdapter<A> implements VoidVisitor<A> {
   public void visit(final AnnotationDeclaration n, final A arg) {
      n.getMembers().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final AnnotationMemberDeclaration n, final A arg) {
      n.getDefaultValue().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getType().accept(this, arg);
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ArrayAccessExpr n, final A arg) {
      n.getIndex().accept(this, arg);
      n.getName().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ArrayCreationExpr n, final A arg) {
      n.getElementType().accept(this, arg);
      n.getInitializer().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getLevels().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ArrayInitializerExpr n, final A arg) {
      n.getValues().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final AssertStmt n, final A arg) {
      n.getCheck().accept(this, arg);
      n.getMessage().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final AssignExpr n, final A arg) {
      n.getTarget().accept(this, arg);
      n.getValue().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final BinaryExpr n, final A arg) {
      n.getLeft().accept(this, arg);
      n.getRight().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final BlockComment n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final BlockStmt n, final A arg) {
      n.getStatements().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final BooleanLiteralExpr n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final BreakStmt n, final A arg) {
      n.getLabel().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final CastExpr n, final A arg) {
      n.getExpression().accept(this, arg);
      n.getType().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final CatchClause n, final A arg) {
      n.getBody().accept((VoidVisitor)this, arg);
      n.getParameter().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final CharLiteralExpr n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ClassExpr n, final A arg) {
      n.getType().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ClassOrInterfaceDeclaration n, final A arg) {
      n.getExtendedTypes().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getImplementedTypes().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getTypeParameters().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getMembers().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ClassOrInterfaceType n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getScope().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getTypeArguments().ifPresent((l) -> {
         l.forEach((v) -> {
            v.accept(this, arg);
         });
      });
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final CompilationUnit n, final A arg) {
      n.getImports().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getModule().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getPackageDeclaration().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getTypes().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ConditionalExpr n, final A arg) {
      n.getCondition().accept(this, arg);
      n.getElseExpr().accept(this, arg);
      n.getThenExpr().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ConstructorDeclaration n, final A arg) {
      n.getBody().accept((VoidVisitor)this, arg);
      n.getName().accept((VoidVisitor)this, arg);
      n.getParameters().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getReceiverParameter().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getThrownExceptions().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getTypeParameters().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ContinueStmt n, final A arg) {
      n.getLabel().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final DoStmt n, final A arg) {
      n.getBody().accept(this, arg);
      n.getCondition().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final DoubleLiteralExpr n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final EmptyStmt n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final EnclosedExpr n, final A arg) {
      n.getInner().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final EnumConstantDeclaration n, final A arg) {
      n.getArguments().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getClassBody().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final EnumDeclaration n, final A arg) {
      n.getEntries().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getImplementedTypes().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getMembers().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ExplicitConstructorInvocationStmt n, final A arg) {
      n.getArguments().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getExpression().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getTypeArguments().ifPresent((l) -> {
         l.forEach((v) -> {
            v.accept(this, arg);
         });
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ExpressionStmt n, final A arg) {
      n.getExpression().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final FieldAccessExpr n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getScope().accept(this, arg);
      n.getTypeArguments().ifPresent((l) -> {
         l.forEach((v) -> {
            v.accept(this, arg);
         });
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final FieldDeclaration n, final A arg) {
      n.getVariables().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ForeachStmt n, final A arg) {
      n.getBody().accept(this, arg);
      n.getIterable().accept(this, arg);
      n.getVariable().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ForStmt n, final A arg) {
      n.getBody().accept(this, arg);
      n.getCompare().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getInitialization().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getUpdate().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final IfStmt n, final A arg) {
      n.getCondition().accept(this, arg);
      n.getElseStmt().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getThenStmt().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final InitializerDeclaration n, final A arg) {
      n.getBody().accept((VoidVisitor)this, arg);
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final InstanceOfExpr n, final A arg) {
      n.getExpression().accept(this, arg);
      n.getType().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final IntegerLiteralExpr n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final JavadocComment n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final LabeledStmt n, final A arg) {
      n.getLabel().accept((VoidVisitor)this, arg);
      n.getStatement().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final LineComment n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final LongLiteralExpr n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final MarkerAnnotationExpr n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final MemberValuePair n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getValue().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final MethodCallExpr n, final A arg) {
      n.getArguments().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getScope().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getTypeArguments().ifPresent((l) -> {
         l.forEach((v) -> {
            v.accept(this, arg);
         });
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final MethodDeclaration n, final A arg) {
      n.getBody().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getType().accept(this, arg);
      n.getName().accept((VoidVisitor)this, arg);
      n.getParameters().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getReceiverParameter().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getThrownExceptions().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getTypeParameters().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final NameExpr n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final NormalAnnotationExpr n, final A arg) {
      n.getPairs().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final NullLiteralExpr n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ObjectCreationExpr n, final A arg) {
      n.getAnonymousClassBody().ifPresent((l) -> {
         l.forEach((v) -> {
            v.accept(this, arg);
         });
      });
      n.getArguments().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getScope().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getType().accept((VoidVisitor)this, arg);
      n.getTypeArguments().ifPresent((l) -> {
         l.forEach((v) -> {
            v.accept(this, arg);
         });
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final PackageDeclaration n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final Parameter n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getType().accept(this, arg);
      n.getVarArgsAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final PrimitiveType n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final Name n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getQualifier().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final SimpleName n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ArrayType n, final A arg) {
      n.getComponentType().accept(this, arg);
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ArrayCreationLevel n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getDimension().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final IntersectionType n, final A arg) {
      n.getElements().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final UnionType n, final A arg) {
      n.getElements().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ReturnStmt n, final A arg) {
      n.getExpression().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final SingleMemberAnnotationExpr n, final A arg) {
      n.getMemberValue().accept(this, arg);
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final StringLiteralExpr n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final SuperExpr n, final A arg) {
      n.getClassExpr().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final SwitchEntryStmt n, final A arg) {
      n.getLabel().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getStatements().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final SwitchStmt n, final A arg) {
      n.getEntries().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getSelector().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final SynchronizedStmt n, final A arg) {
      n.getBody().accept((VoidVisitor)this, arg);
      n.getExpression().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ThisExpr n, final A arg) {
      n.getClassExpr().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ThrowStmt n, final A arg) {
      n.getExpression().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final TryStmt n, final A arg) {
      n.getCatchClauses().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getFinallyBlock().ifPresent((l) -> {
         l.accept((VoidVisitor)this, arg);
      });
      n.getResources().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getTryBlock().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final LocalClassDeclarationStmt n, final A arg) {
      n.getClassDeclaration().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final TypeParameter n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getTypeBound().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final UnaryExpr n, final A arg) {
      n.getExpression().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final UnknownType n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final VariableDeclarationExpr n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getVariables().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final VariableDeclarator n, final A arg) {
      n.getInitializer().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getType().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final VoidType n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final WhileStmt n, final A arg) {
      n.getBody().accept(this, arg);
      n.getCondition().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final WildcardType n, final A arg) {
      n.getExtendedType().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getSuperType().ifPresent((l) -> {
         l.accept(this, arg);
      });
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final LambdaExpr n, final A arg) {
      n.getBody().accept(this, arg);
      n.getParameters().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final MethodReferenceExpr n, final A arg) {
      n.getScope().accept(this, arg);
      n.getTypeArguments().ifPresent((l) -> {
         l.forEach((v) -> {
            v.accept(this, arg);
         });
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final TypeExpr n, final A arg) {
      n.getType().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(NodeList n, A arg) {
      Iterator var3 = n.iterator();

      while(var3.hasNext()) {
         Object node = var3.next();
         ((Node)node).accept(this, arg);
      }

   }

   public void visit(final ImportDeclaration n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ModuleDeclaration n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getModuleStmts().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ModuleRequiresStmt n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ModuleExportsStmt n, final A arg) {
      n.getModuleNames().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ModuleProvidesStmt n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getWith().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ModuleUsesStmt n, final A arg) {
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ModuleOpensStmt n, final A arg) {
      n.getModuleNames().forEach((p) -> {
         p.accept((VoidVisitor)this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final UnparsableStmt n, final A arg) {
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final ReceiverParameter n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getName().accept((VoidVisitor)this, arg);
      n.getType().accept(this, arg);
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }

   public void visit(final VarType n, final A arg) {
      n.getAnnotations().forEach((p) -> {
         p.accept(this, arg);
      });
      n.getComment().ifPresent((l) -> {
         l.accept(this, arg);
      });
   }
}
