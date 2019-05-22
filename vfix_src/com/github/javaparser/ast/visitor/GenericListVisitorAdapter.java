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
import com.github.javaparser.ast.comments.Comment;
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
import com.github.javaparser.ast.expr.Expression;
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
import com.github.javaparser.ast.stmt.Statement;
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
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class GenericListVisitorAdapter<R, A> implements GenericVisitor<List<R>, A> {
   public List<R> visit(final AnnotationDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getMembers().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final AnnotationMemberDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getDefaultValue().isPresent()) {
         tmp = (List)((Expression)n.getDefaultValue().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ArrayAccessExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getIndex().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ArrayCreationExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getElementType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getInitializer().isPresent()) {
         tmp = (List)((ArrayInitializerExpr)n.getInitializer().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getLevels().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ArrayCreationLevel n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getDimension().isPresent()) {
         tmp = (List)((Expression)n.getDimension().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ArrayInitializerExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getValues().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ArrayType n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getComponentType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final AssertStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getCheck().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getMessage().isPresent()) {
         tmp = (List)((Expression)n.getMessage().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final AssignExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getTarget().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getValue().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final BinaryExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getLeft().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getRight().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final BlockComment n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final BlockStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getStatements().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final BooleanLiteralExpr n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final BreakStmt n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getLabel().isPresent()) {
         tmp = (List)((SimpleName)n.getLabel().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final CastExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getExpression().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final CatchClause n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getParameter().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final CharLiteralExpr n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ClassExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ClassOrInterfaceDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getExtendedTypes().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getImplementedTypes().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getTypeParameters().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getMembers().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ClassOrInterfaceType n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getScope().isPresent()) {
         tmp = (List)((ClassOrInterfaceType)n.getScope().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getTypeArguments().isPresent()) {
         tmp = (List)((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final CompilationUnit n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getImports().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getModule().isPresent()) {
         tmp = (List)((ModuleDeclaration)n.getModule().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getPackageDeclaration().isPresent()) {
         tmp = (List)((PackageDeclaration)n.getPackageDeclaration().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getTypes().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ConditionalExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getCondition().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getElseExpr().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getThenExpr().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ConstructorDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getParameters().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getReceiverParameter().isPresent()) {
         tmp = (List)((ReceiverParameter)n.getReceiverParameter().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getThrownExceptions().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getTypeParameters().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ContinueStmt n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getLabel().isPresent()) {
         tmp = (List)((SimpleName)n.getLabel().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final DoStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getCondition().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final DoubleLiteralExpr n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final EmptyStmt n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final EnclosedExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getInner().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final EnumConstantDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getArguments().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getClassBody().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final EnumDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getEntries().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getImplementedTypes().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getMembers().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ExplicitConstructorInvocationStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getArguments().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getExpression().isPresent()) {
         tmp = (List)((Expression)n.getExpression().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getTypeArguments().isPresent()) {
         tmp = (List)((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ExpressionStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getExpression().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final FieldAccessExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getScope().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getTypeArguments().isPresent()) {
         tmp = (List)((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final FieldDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getVariables().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ForStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getCompare().isPresent()) {
         tmp = (List)((Expression)n.getCompare().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getInitialization().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getUpdate().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ForeachStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getIterable().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getVariable().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final IfStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getCondition().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getElseStmt().isPresent()) {
         tmp = (List)((Statement)n.getElseStmt().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getThenStmt().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ImportDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final InitializerDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final InstanceOfExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getExpression().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final IntegerLiteralExpr n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final IntersectionType n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getElements().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final JavadocComment n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final LabeledStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getLabel().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getStatement().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final LambdaExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getParameters().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final LineComment n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final LocalClassDeclarationStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getClassDeclaration().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final LongLiteralExpr n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final MarkerAnnotationExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final MemberValuePair n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getValue().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final MethodCallExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getArguments().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getScope().isPresent()) {
         tmp = (List)((Expression)n.getScope().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getTypeArguments().isPresent()) {
         tmp = (List)((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final MethodDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getBody().isPresent()) {
         tmp = (List)((BlockStmt)n.getBody().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getParameters().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getReceiverParameter().isPresent()) {
         tmp = (List)((ReceiverParameter)n.getReceiverParameter().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getThrownExceptions().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getTypeParameters().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final MethodReferenceExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getScope().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getTypeArguments().isPresent()) {
         tmp = (List)((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final NameExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final Name n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getQualifier().isPresent()) {
         tmp = (List)((Name)n.getQualifier().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final NormalAnnotationExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getPairs().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final NullLiteralExpr n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ObjectCreationExpr n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getAnonymousClassBody().isPresent()) {
         tmp = (List)((NodeList)n.getAnonymousClassBody().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getArguments().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getScope().isPresent()) {
         tmp = (List)((Expression)n.getScope().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getType().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getTypeArguments().isPresent()) {
         tmp = (List)((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final PackageDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final Parameter n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getVarArgsAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final PrimitiveType n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ReturnStmt n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getExpression().isPresent()) {
         tmp = (List)((Expression)n.getExpression().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final SimpleName n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final SingleMemberAnnotationExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getMemberValue().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final StringLiteralExpr n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final SuperExpr n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getClassExpr().isPresent()) {
         tmp = (List)((Expression)n.getClassExpr().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final SwitchEntryStmt n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getLabel().isPresent()) {
         tmp = (List)((Expression)n.getLabel().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getStatements().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final SwitchStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getEntries().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getSelector().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final SynchronizedStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getExpression().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ThisExpr n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getClassExpr().isPresent()) {
         tmp = (List)((Expression)n.getClassExpr().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ThrowStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getExpression().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final TryStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getCatchClauses().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getFinallyBlock().isPresent()) {
         tmp = (List)((BlockStmt)n.getFinallyBlock().get()).accept((GenericVisitor)this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getResources().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getTryBlock().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final TypeExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final TypeParameter n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getTypeBound().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final UnaryExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getExpression().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final UnionType n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getElements().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final UnknownType n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final VariableDeclarationExpr n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getVariables().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final VariableDeclarator n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getInitializer().isPresent()) {
         tmp = (List)((Expression)n.getInitializer().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final VoidType n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final WhileStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getBody().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getCondition().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final WildcardType n, final A arg) {
      List<R> result = new ArrayList();
      List tmp;
      if (n.getExtendedType().isPresent()) {
         tmp = (List)((ReferenceType)n.getExtendedType().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      if (n.getSuperType().isPresent()) {
         tmp = (List)((ReferenceType)n.getSuperType().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(NodeList n, A arg) {
      return (List)n.stream().filter(Objects::nonNull).flatMap((v) -> {
         return ((List)v.accept(this, arg)).stream();
      }).collect(Collectors.toList());
   }

   public List<R> visit(final ModuleDeclaration n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getModuleStmts().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ModuleExportsStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getModuleNames().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ModuleOpensStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getModuleNames().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ModuleProvidesStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getWith().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ModuleRequiresStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ModuleUsesStmt n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final UnparsableStmt n, final A arg) {
      List<R> result = new ArrayList();
      if (n.getComment().isPresent()) {
         List<R> tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final ReceiverParameter n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getName().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      tmp = (List)n.getType().accept(this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }

   public List<R> visit(final VarType n, final A arg) {
      List<R> result = new ArrayList();
      List<R> tmp = (List)n.getAnnotations().accept((GenericVisitor)this, arg);
      if (tmp != null) {
         result.addAll(tmp);
      }

      if (n.getComment().isPresent()) {
         tmp = (List)((Comment)n.getComment().get()).accept(this, arg);
         if (tmp != null) {
            result.addAll(tmp);
         }
      }

      return result;
   }
}
