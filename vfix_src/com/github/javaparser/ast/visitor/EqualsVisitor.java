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
import java.util.List;
import java.util.Optional;

public class EqualsVisitor implements GenericVisitor<Boolean, Visitable> {
   private static final EqualsVisitor SINGLETON = new EqualsVisitor();

   public static boolean equals(final Node n, final Node n2) {
      return SINGLETON.nodeEquals(n, n2);
   }

   private EqualsVisitor() {
   }

   private boolean commonNodeEquality(Node n, Node n2) {
      return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : this.nodesEquals(n.getOrphanComments(), n2.getOrphanComments());
   }

   private <T extends Node> boolean nodesEquals(final List<T> nodes1, final List<T> nodes2) {
      if (nodes1 == null) {
         return nodes2 == null;
      } else if (nodes2 == null) {
         return false;
      } else if (nodes1.size() != nodes2.size()) {
         return false;
      } else {
         for(int i = 0; i < nodes1.size(); ++i) {
            if (!this.nodeEquals((Node)nodes1.get(i), (Node)nodes2.get(i))) {
               return false;
            }
         }

         return true;
      }
   }

   private <N extends Node> boolean nodesEquals(NodeList<N> n, NodeList<N> n2) {
      if (n == n2) {
         return true;
      } else if (n != null && n2 != null) {
         if (n.size() != n2.size()) {
            return false;
         } else {
            for(int i = 0; i < n.size(); ++i) {
               if (!this.nodeEquals(n.get(i), n2.get(i))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private <T extends Node> boolean nodeEquals(final T n, final T n2) {
      if (n == n2) {
         return true;
      } else if (n != null && n2 != null) {
         if (n.getClass() != n2.getClass()) {
            return false;
         } else {
            return !this.commonNodeEquality(n, n2) ? false : (Boolean)n.accept(this, n2);
         }
      } else {
         return false;
      }
   }

   private <T extends Node> boolean nodeEquals(final Optional<T> n, final Optional<T> n2) {
      return this.nodeEquals((Node)n.orElse((Object)null), (Node)n2.orElse((Object)null));
   }

   private <T extends Node> boolean nodesEquals(final Optional<NodeList<T>> n, final Optional<NodeList<T>> n2) {
      return this.nodesEquals((NodeList)n.orElse((Object)null), (NodeList)n2.orElse((Object)null));
   }

   private boolean objEquals(final Object n, final Object n2) {
      if (n == n2) {
         return true;
      } else {
         return n != null && n2 != null ? n.equals(n2) : false;
      }
   }

   public Boolean visit(final CompilationUnit n, final Visitable arg) {
      CompilationUnit n2 = (CompilationUnit)arg;
      if (!this.nodesEquals(n.getImports(), n2.getImports())) {
         return false;
      } else if (!this.nodeEquals(n.getModule(), n2.getModule())) {
         return false;
      } else if (!this.nodeEquals(n.getPackageDeclaration(), n2.getPackageDeclaration())) {
         return false;
      } else if (!this.nodesEquals(n.getTypes(), n2.getTypes())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final PackageDeclaration n, final Visitable arg) {
      PackageDeclaration n2 = (PackageDeclaration)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final TypeParameter n, final Visitable arg) {
      TypeParameter n2 = (TypeParameter)arg;
      if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeBound(), n2.getTypeBound())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final LineComment n, final Visitable arg) {
      LineComment n2 = (LineComment)arg;
      if (!this.objEquals(n.getContent(), n2.getContent())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final BlockComment n, final Visitable arg) {
      BlockComment n2 = (BlockComment)arg;
      if (!this.objEquals(n.getContent(), n2.getContent())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ClassOrInterfaceDeclaration n, final Visitable arg) {
      ClassOrInterfaceDeclaration n2 = (ClassOrInterfaceDeclaration)arg;
      if (!this.nodesEquals(n.getExtendedTypes(), n2.getExtendedTypes())) {
         return false;
      } else if (!this.nodesEquals(n.getImplementedTypes(), n2.getImplementedTypes())) {
         return false;
      } else if (!this.objEquals(n.isInterface(), n2.isInterface())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeParameters(), n2.getTypeParameters())) {
         return false;
      } else if (!this.nodesEquals(n.getMembers(), n2.getMembers())) {
         return false;
      } else if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final EnumDeclaration n, final Visitable arg) {
      EnumDeclaration n2 = (EnumDeclaration)arg;
      if (!this.nodesEquals(n.getEntries(), n2.getEntries())) {
         return false;
      } else if (!this.nodesEquals(n.getImplementedTypes(), n2.getImplementedTypes())) {
         return false;
      } else if (!this.nodesEquals(n.getMembers(), n2.getMembers())) {
         return false;
      } else if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final EnumConstantDeclaration n, final Visitable arg) {
      EnumConstantDeclaration n2 = (EnumConstantDeclaration)arg;
      if (!this.nodesEquals(n.getArguments(), n2.getArguments())) {
         return false;
      } else if (!this.nodesEquals(n.getClassBody(), n2.getClassBody())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final AnnotationDeclaration n, final Visitable arg) {
      AnnotationDeclaration n2 = (AnnotationDeclaration)arg;
      if (!this.nodesEquals(n.getMembers(), n2.getMembers())) {
         return false;
      } else if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final AnnotationMemberDeclaration n, final Visitable arg) {
      AnnotationMemberDeclaration n2 = (AnnotationMemberDeclaration)arg;
      if (!this.nodeEquals(n.getDefaultValue(), n2.getDefaultValue())) {
         return false;
      } else if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final FieldDeclaration n, final Visitable arg) {
      FieldDeclaration n2 = (FieldDeclaration)arg;
      if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodesEquals(n.getVariables(), n2.getVariables())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final VariableDeclarator n, final Visitable arg) {
      VariableDeclarator n2 = (VariableDeclarator)arg;
      if (!this.nodeEquals(n.getInitializer(), n2.getInitializer())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ConstructorDeclaration n, final Visitable arg) {
      ConstructorDeclaration n2 = (ConstructorDeclaration)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodesEquals(n.getParameters(), n2.getParameters())) {
         return false;
      } else if (!this.nodeEquals(n.getReceiverParameter(), n2.getReceiverParameter())) {
         return false;
      } else if (!this.nodesEquals(n.getThrownExceptions(), n2.getThrownExceptions())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeParameters(), n2.getTypeParameters())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final MethodDeclaration n, final Visitable arg) {
      MethodDeclaration n2 = (MethodDeclaration)arg;
      if (!this.nodeEquals(n.getBody(), n2.getBody())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodesEquals(n.getParameters(), n2.getParameters())) {
         return false;
      } else if (!this.nodeEquals(n.getReceiverParameter(), n2.getReceiverParameter())) {
         return false;
      } else if (!this.nodesEquals(n.getThrownExceptions(), n2.getThrownExceptions())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeParameters(), n2.getTypeParameters())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final Parameter n, final Visitable arg) {
      Parameter n2 = (Parameter)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else if (!this.objEquals(n.isVarArgs(), n2.isVarArgs())) {
         return false;
      } else if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else if (!this.nodesEquals(n.getVarArgsAnnotations(), n2.getVarArgsAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final InitializerDeclaration n, final Visitable arg) {
      InitializerDeclaration n2 = (InitializerDeclaration)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.objEquals(n.isStatic(), n2.isStatic())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final JavadocComment n, final Visitable arg) {
      JavadocComment n2 = (JavadocComment)arg;
      if (!this.objEquals(n.getContent(), n2.getContent())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ClassOrInterfaceType n, final Visitable arg) {
      ClassOrInterfaceType n2 = (ClassOrInterfaceType)arg;
      if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodeEquals(n.getScope(), n2.getScope())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeArguments(), n2.getTypeArguments())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final PrimitiveType n, final Visitable arg) {
      PrimitiveType n2 = (PrimitiveType)arg;
      if (!this.objEquals(n.getType(), n2.getType())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ArrayType n, final Visitable arg) {
      ArrayType n2 = (ArrayType)arg;
      if (!this.nodeEquals((Node)n.getComponentType(), (Node)n2.getComponentType())) {
         return false;
      } else if (!this.objEquals(n.getOrigin(), n2.getOrigin())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ArrayCreationLevel n, final Visitable arg) {
      ArrayCreationLevel n2 = (ArrayCreationLevel)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else if (!this.nodeEquals(n.getDimension(), n2.getDimension())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final IntersectionType n, final Visitable arg) {
      IntersectionType n2 = (IntersectionType)arg;
      if (!this.nodesEquals(n.getElements(), n2.getElements())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final UnionType n, final Visitable arg) {
      UnionType n2 = (UnionType)arg;
      if (!this.nodesEquals(n.getElements(), n2.getElements())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final VoidType n, final Visitable arg) {
      VoidType n2 = (VoidType)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final WildcardType n, final Visitable arg) {
      WildcardType n2 = (WildcardType)arg;
      if (!this.nodeEquals(n.getExtendedType(), n2.getExtendedType())) {
         return false;
      } else if (!this.nodeEquals(n.getSuperType(), n2.getSuperType())) {
         return false;
      } else if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final UnknownType n, final Visitable arg) {
      UnknownType n2 = (UnknownType)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ArrayAccessExpr n, final Visitable arg) {
      ArrayAccessExpr n2 = (ArrayAccessExpr)arg;
      if (!this.nodeEquals((Node)n.getIndex(), (Node)n2.getIndex())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ArrayCreationExpr n, final Visitable arg) {
      ArrayCreationExpr n2 = (ArrayCreationExpr)arg;
      if (!this.nodeEquals((Node)n.getElementType(), (Node)n2.getElementType())) {
         return false;
      } else if (!this.nodeEquals(n.getInitializer(), n2.getInitializer())) {
         return false;
      } else if (!this.nodesEquals(n.getLevels(), n2.getLevels())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ArrayInitializerExpr n, final Visitable arg) {
      ArrayInitializerExpr n2 = (ArrayInitializerExpr)arg;
      if (!this.nodesEquals(n.getValues(), n2.getValues())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final AssignExpr n, final Visitable arg) {
      AssignExpr n2 = (AssignExpr)arg;
      if (!this.objEquals(n.getOperator(), n2.getOperator())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getTarget(), (Node)n2.getTarget())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getValue(), (Node)n2.getValue())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final BinaryExpr n, final Visitable arg) {
      BinaryExpr n2 = (BinaryExpr)arg;
      if (!this.nodeEquals((Node)n.getLeft(), (Node)n2.getLeft())) {
         return false;
      } else if (!this.objEquals(n.getOperator(), n2.getOperator())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getRight(), (Node)n2.getRight())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final CastExpr n, final Visitable arg) {
      CastExpr n2 = (CastExpr)arg;
      if (!this.nodeEquals((Node)n.getExpression(), (Node)n2.getExpression())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ClassExpr n, final Visitable arg) {
      ClassExpr n2 = (ClassExpr)arg;
      if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ConditionalExpr n, final Visitable arg) {
      ConditionalExpr n2 = (ConditionalExpr)arg;
      if (!this.nodeEquals((Node)n.getCondition(), (Node)n2.getCondition())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getElseExpr(), (Node)n2.getElseExpr())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getThenExpr(), (Node)n2.getThenExpr())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final EnclosedExpr n, final Visitable arg) {
      EnclosedExpr n2 = (EnclosedExpr)arg;
      if (!this.nodeEquals((Node)n.getInner(), (Node)n2.getInner())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final FieldAccessExpr n, final Visitable arg) {
      FieldAccessExpr n2 = (FieldAccessExpr)arg;
      if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getScope(), (Node)n2.getScope())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeArguments(), n2.getTypeArguments())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final InstanceOfExpr n, final Visitable arg) {
      InstanceOfExpr n2 = (InstanceOfExpr)arg;
      if (!this.nodeEquals((Node)n.getExpression(), (Node)n2.getExpression())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final StringLiteralExpr n, final Visitable arg) {
      StringLiteralExpr n2 = (StringLiteralExpr)arg;
      if (!this.objEquals(n.getValue(), n2.getValue())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final IntegerLiteralExpr n, final Visitable arg) {
      IntegerLiteralExpr n2 = (IntegerLiteralExpr)arg;
      if (!this.objEquals(n.getValue(), n2.getValue())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final LongLiteralExpr n, final Visitable arg) {
      LongLiteralExpr n2 = (LongLiteralExpr)arg;
      if (!this.objEquals(n.getValue(), n2.getValue())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final CharLiteralExpr n, final Visitable arg) {
      CharLiteralExpr n2 = (CharLiteralExpr)arg;
      if (!this.objEquals(n.getValue(), n2.getValue())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final DoubleLiteralExpr n, final Visitable arg) {
      DoubleLiteralExpr n2 = (DoubleLiteralExpr)arg;
      if (!this.objEquals(n.getValue(), n2.getValue())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final BooleanLiteralExpr n, final Visitable arg) {
      BooleanLiteralExpr n2 = (BooleanLiteralExpr)arg;
      if (!this.objEquals(n.getValue(), n2.getValue())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final NullLiteralExpr n, final Visitable arg) {
      NullLiteralExpr n2 = (NullLiteralExpr)arg;
      return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
   }

   public Boolean visit(final MethodCallExpr n, final Visitable arg) {
      MethodCallExpr n2 = (MethodCallExpr)arg;
      if (!this.nodesEquals(n.getArguments(), n2.getArguments())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodeEquals(n.getScope(), n2.getScope())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeArguments(), n2.getTypeArguments())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final NameExpr n, final Visitable arg) {
      NameExpr n2 = (NameExpr)arg;
      if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ObjectCreationExpr n, final Visitable arg) {
      ObjectCreationExpr n2 = (ObjectCreationExpr)arg;
      if (!this.nodesEquals(n.getAnonymousClassBody(), n2.getAnonymousClassBody())) {
         return false;
      } else if (!this.nodesEquals(n.getArguments(), n2.getArguments())) {
         return false;
      } else if (!this.nodeEquals(n.getScope(), n2.getScope())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeArguments(), n2.getTypeArguments())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final Name n, final Visitable arg) {
      Name n2 = (Name)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else if (!this.objEquals(n.getIdentifier(), n2.getIdentifier())) {
         return false;
      } else if (!this.nodeEquals(n.getQualifier(), n2.getQualifier())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final SimpleName n, final Visitable arg) {
      SimpleName n2 = (SimpleName)arg;
      if (!this.objEquals(n.getIdentifier(), n2.getIdentifier())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ThisExpr n, final Visitable arg) {
      ThisExpr n2 = (ThisExpr)arg;
      if (!this.nodeEquals(n.getClassExpr(), n2.getClassExpr())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final SuperExpr n, final Visitable arg) {
      SuperExpr n2 = (SuperExpr)arg;
      if (!this.nodeEquals(n.getClassExpr(), n2.getClassExpr())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final UnaryExpr n, final Visitable arg) {
      UnaryExpr n2 = (UnaryExpr)arg;
      if (!this.nodeEquals((Node)n.getExpression(), (Node)n2.getExpression())) {
         return false;
      } else if (!this.objEquals(n.getOperator(), n2.getOperator())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final VariableDeclarationExpr n, final Visitable arg) {
      VariableDeclarationExpr n2 = (VariableDeclarationExpr)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodesEquals(n.getVariables(), n2.getVariables())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final MarkerAnnotationExpr n, final Visitable arg) {
      MarkerAnnotationExpr n2 = (MarkerAnnotationExpr)arg;
      if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final SingleMemberAnnotationExpr n, final Visitable arg) {
      SingleMemberAnnotationExpr n2 = (SingleMemberAnnotationExpr)arg;
      if (!this.nodeEquals((Node)n.getMemberValue(), (Node)n2.getMemberValue())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final NormalAnnotationExpr n, final Visitable arg) {
      NormalAnnotationExpr n2 = (NormalAnnotationExpr)arg;
      if (!this.nodesEquals(n.getPairs(), n2.getPairs())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final MemberValuePair n, final Visitable arg) {
      MemberValuePair n2 = (MemberValuePair)arg;
      if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getValue(), (Node)n2.getValue())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ExplicitConstructorInvocationStmt n, final Visitable arg) {
      ExplicitConstructorInvocationStmt n2 = (ExplicitConstructorInvocationStmt)arg;
      if (!this.nodesEquals(n.getArguments(), n2.getArguments())) {
         return false;
      } else if (!this.nodeEquals(n.getExpression(), n2.getExpression())) {
         return false;
      } else if (!this.objEquals(n.isThis(), n2.isThis())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeArguments(), n2.getTypeArguments())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final LocalClassDeclarationStmt n, final Visitable arg) {
      LocalClassDeclarationStmt n2 = (LocalClassDeclarationStmt)arg;
      if (!this.nodeEquals((Node)n.getClassDeclaration(), (Node)n2.getClassDeclaration())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final AssertStmt n, final Visitable arg) {
      AssertStmt n2 = (AssertStmt)arg;
      if (!this.nodeEquals((Node)n.getCheck(), (Node)n2.getCheck())) {
         return false;
      } else if (!this.nodeEquals(n.getMessage(), n2.getMessage())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final BlockStmt n, final Visitable arg) {
      BlockStmt n2 = (BlockStmt)arg;
      if (!this.nodesEquals(n.getStatements(), n2.getStatements())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final LabeledStmt n, final Visitable arg) {
      LabeledStmt n2 = (LabeledStmt)arg;
      if (!this.nodeEquals((Node)n.getLabel(), (Node)n2.getLabel())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getStatement(), (Node)n2.getStatement())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final EmptyStmt n, final Visitable arg) {
      EmptyStmt n2 = (EmptyStmt)arg;
      return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
   }

   public Boolean visit(final ExpressionStmt n, final Visitable arg) {
      ExpressionStmt n2 = (ExpressionStmt)arg;
      if (!this.nodeEquals((Node)n.getExpression(), (Node)n2.getExpression())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final SwitchStmt n, final Visitable arg) {
      SwitchStmt n2 = (SwitchStmt)arg;
      if (!this.nodesEquals(n.getEntries(), n2.getEntries())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getSelector(), (Node)n2.getSelector())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final SwitchEntryStmt n, final Visitable arg) {
      SwitchEntryStmt n2 = (SwitchEntryStmt)arg;
      if (!this.nodeEquals(n.getLabel(), n2.getLabel())) {
         return false;
      } else if (!this.nodesEquals(n.getStatements(), n2.getStatements())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final BreakStmt n, final Visitable arg) {
      BreakStmt n2 = (BreakStmt)arg;
      if (!this.nodeEquals(n.getLabel(), n2.getLabel())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ReturnStmt n, final Visitable arg) {
      ReturnStmt n2 = (ReturnStmt)arg;
      if (!this.nodeEquals(n.getExpression(), n2.getExpression())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final IfStmt n, final Visitable arg) {
      IfStmt n2 = (IfStmt)arg;
      if (!this.nodeEquals((Node)n.getCondition(), (Node)n2.getCondition())) {
         return false;
      } else if (!this.nodeEquals(n.getElseStmt(), n2.getElseStmt())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getThenStmt(), (Node)n2.getThenStmt())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final WhileStmt n, final Visitable arg) {
      WhileStmt n2 = (WhileStmt)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getCondition(), (Node)n2.getCondition())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ContinueStmt n, final Visitable arg) {
      ContinueStmt n2 = (ContinueStmt)arg;
      if (!this.nodeEquals(n.getLabel(), n2.getLabel())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final DoStmt n, final Visitable arg) {
      DoStmt n2 = (DoStmt)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getCondition(), (Node)n2.getCondition())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ForeachStmt n, final Visitable arg) {
      ForeachStmt n2 = (ForeachStmt)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getIterable(), (Node)n2.getIterable())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getVariable(), (Node)n2.getVariable())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ForStmt n, final Visitable arg) {
      ForStmt n2 = (ForStmt)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.nodeEquals(n.getCompare(), n2.getCompare())) {
         return false;
      } else if (!this.nodesEquals(n.getInitialization(), n2.getInitialization())) {
         return false;
      } else if (!this.nodesEquals(n.getUpdate(), n2.getUpdate())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ThrowStmt n, final Visitable arg) {
      ThrowStmt n2 = (ThrowStmt)arg;
      if (!this.nodeEquals((Node)n.getExpression(), (Node)n2.getExpression())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final SynchronizedStmt n, final Visitable arg) {
      SynchronizedStmt n2 = (SynchronizedStmt)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getExpression(), (Node)n2.getExpression())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final TryStmt n, final Visitable arg) {
      TryStmt n2 = (TryStmt)arg;
      if (!this.nodesEquals(n.getCatchClauses(), n2.getCatchClauses())) {
         return false;
      } else if (!this.nodeEquals(n.getFinallyBlock(), n2.getFinallyBlock())) {
         return false;
      } else if (!this.nodesEquals(n.getResources(), n2.getResources())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getTryBlock(), (Node)n2.getTryBlock())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final CatchClause n, final Visitable arg) {
      CatchClause n2 = (CatchClause)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getParameter(), (Node)n2.getParameter())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final LambdaExpr n, final Visitable arg) {
      LambdaExpr n2 = (LambdaExpr)arg;
      if (!this.nodeEquals((Node)n.getBody(), (Node)n2.getBody())) {
         return false;
      } else if (!this.objEquals(n.isEnclosingParameters(), n2.isEnclosingParameters())) {
         return false;
      } else if (!this.nodesEquals(n.getParameters(), n2.getParameters())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final MethodReferenceExpr n, final Visitable arg) {
      MethodReferenceExpr n2 = (MethodReferenceExpr)arg;
      if (!this.objEquals(n.getIdentifier(), n2.getIdentifier())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getScope(), (Node)n2.getScope())) {
         return false;
      } else if (!this.nodesEquals(n.getTypeArguments(), n2.getTypeArguments())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final TypeExpr n, final Visitable arg) {
      TypeExpr n2 = (TypeExpr)arg;
      if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ImportDeclaration n, final Visitable arg) {
      ImportDeclaration n2 = (ImportDeclaration)arg;
      if (!this.objEquals(n.isAsterisk(), n2.isAsterisk())) {
         return false;
      } else if (!this.objEquals(n.isStatic(), n2.isStatic())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(NodeList n, Visitable arg) {
      return this.nodesEquals(n, (NodeList)arg);
   }

   public Boolean visit(final ModuleDeclaration n, final Visitable arg) {
      ModuleDeclaration n2 = (ModuleDeclaration)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else if (!this.objEquals(n.isOpen(), n2.isOpen())) {
         return false;
      } else if (!this.nodesEquals(n.getModuleStmts(), n2.getModuleStmts())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ModuleRequiresStmt n, final Visitable arg) {
      ModuleRequiresStmt n2 = (ModuleRequiresStmt)arg;
      if (!this.objEquals(n.getModifiers(), n2.getModifiers())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ModuleExportsStmt n, final Visitable arg) {
      ModuleExportsStmt n2 = (ModuleExportsStmt)arg;
      if (!this.nodesEquals(n.getModuleNames(), n2.getModuleNames())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ModuleProvidesStmt n, final Visitable arg) {
      ModuleProvidesStmt n2 = (ModuleProvidesStmt)arg;
      if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodesEquals(n.getWith(), n2.getWith())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ModuleUsesStmt n, final Visitable arg) {
      ModuleUsesStmt n2 = (ModuleUsesStmt)arg;
      if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final ModuleOpensStmt n, final Visitable arg) {
      ModuleOpensStmt n2 = (ModuleOpensStmt)arg;
      if (!this.nodesEquals(n.getModuleNames(), n2.getModuleNames())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final UnparsableStmt n, final Visitable arg) {
      UnparsableStmt n2 = (UnparsableStmt)arg;
      return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
   }

   public Boolean visit(final ReceiverParameter n, final Visitable arg) {
      ReceiverParameter n2 = (ReceiverParameter)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getName(), (Node)n2.getName())) {
         return false;
      } else if (!this.nodeEquals((Node)n.getType(), (Node)n2.getType())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }

   public Boolean visit(final VarType n, final Visitable arg) {
      VarType n2 = (VarType)arg;
      if (!this.nodesEquals(n.getAnnotations(), n2.getAnnotations())) {
         return false;
      } else {
         return !this.nodeEquals(n.getComment(), n2.getComment()) ? false : true;
      }
   }
}
