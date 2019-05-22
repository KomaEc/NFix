package com.github.javaparser.ast.visitor;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
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
import com.github.javaparser.ast.modules.ModuleStmt;
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
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.utils.Pair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ModifierVisitor<A> implements GenericVisitor<Visitable, A> {
   public Visitable visit(final AnnotationDeclaration n, final A arg) {
      NodeList<BodyDeclaration<?>> members = this.modifyList(n.getMembers(), arg);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setMembers(members);
         n.setName(name);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final AnnotationMemberDeclaration n, final A arg) {
      Expression defaultValue = (Expression)n.getDefaultValue().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      Type type = (Type)n.getType().accept(this, arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name != null && type != null) {
         n.setDefaultValue(defaultValue);
         n.setName(name);
         n.setType(type);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final ArrayAccessExpr n, final A arg) {
      Expression index = (Expression)n.getIndex().accept(this, arg);
      Expression name = (Expression)n.getName().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (index != null && name != null) {
         n.setIndex(index);
         n.setName(name);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final ArrayCreationExpr n, final A arg) {
      Type elementType = (Type)n.getElementType().accept(this, arg);
      ArrayInitializerExpr initializer = (ArrayInitializerExpr)n.getInitializer().map((s) -> {
         return (ArrayInitializerExpr)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      NodeList<ArrayCreationLevel> levels = this.modifyList(n.getLevels(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (elementType != null && !levels.isEmpty()) {
         n.setElementType(elementType);
         n.setInitializer(initializer);
         n.setLevels(levels);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final ArrayInitializerExpr n, final A arg) {
      NodeList<Expression> values = this.modifyList(n.getValues(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setValues(values);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final AssertStmt n, final A arg) {
      Expression check = (Expression)n.getCheck().accept(this, arg);
      Expression message = (Expression)n.getMessage().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (check == null) {
         return null;
      } else {
         n.setCheck(check);
         n.setMessage(message);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final AssignExpr n, final A arg) {
      Expression target = (Expression)n.getTarget().accept(this, arg);
      Expression value = (Expression)n.getValue().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (target != null && value != null) {
         n.setTarget(target);
         n.setValue(value);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final BinaryExpr n, final A arg) {
      Expression left = (Expression)n.getLeft().accept(this, arg);
      Expression right = (Expression)n.getRight().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (left == null) {
         return right;
      } else if (right == null) {
         return left;
      } else {
         n.setLeft(left);
         n.setRight(right);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final BlockStmt n, final A arg) {
      NodeList<Statement> statements = this.modifyList(n.getStatements(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setStatements(statements);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final BooleanLiteralExpr n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final BreakStmt n, final A arg) {
      SimpleName label = (SimpleName)n.getLabel().map((s) -> {
         return (SimpleName)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setLabel(label);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final CastExpr n, final A arg) {
      Expression expression = (Expression)n.getExpression().accept(this, arg);
      Type type = (Type)n.getType().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (expression != null && type != null) {
         n.setExpression(expression);
         n.setType(type);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final CatchClause n, final A arg) {
      BlockStmt body = (BlockStmt)n.getBody().accept((GenericVisitor)this, arg);
      Parameter parameter = (Parameter)n.getParameter().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body != null && parameter != null) {
         n.setBody(body);
         n.setParameter(parameter);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final CharLiteralExpr n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final ClassExpr n, final A arg) {
      Type type = (Type)n.getType().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (type == null) {
         return null;
      } else {
         n.setType(type);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ClassOrInterfaceDeclaration n, final A arg) {
      NodeList<ClassOrInterfaceType> extendedTypes = this.modifyList(n.getExtendedTypes(), arg);
      NodeList<ClassOrInterfaceType> implementedTypes = this.modifyList(n.getImplementedTypes(), arg);
      NodeList<TypeParameter> typeParameters = this.modifyList(n.getTypeParameters(), arg);
      NodeList<BodyDeclaration<?>> members = this.modifyList(n.getMembers(), arg);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setExtendedTypes(extendedTypes);
         n.setImplementedTypes(implementedTypes);
         n.setTypeParameters(typeParameters);
         n.setMembers(members);
         n.setName(name);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ClassOrInterfaceType n, final A arg) {
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      ClassOrInterfaceType scope = (ClassOrInterfaceType)n.getScope().map((s) -> {
         return (ClassOrInterfaceType)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      NodeList<Type> typeArguments = this.modifyList(n.getTypeArguments(), arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setName(name);
         n.setScope(scope);
         n.setTypeArguments(typeArguments);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final CompilationUnit n, final A arg) {
      NodeList<ImportDeclaration> imports = this.modifyList(n.getImports(), arg);
      ModuleDeclaration module = (ModuleDeclaration)n.getModule().map((s) -> {
         return (ModuleDeclaration)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      PackageDeclaration packageDeclaration = (PackageDeclaration)n.getPackageDeclaration().map((s) -> {
         return (PackageDeclaration)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      NodeList<TypeDeclaration<?>> types = this.modifyList(n.getTypes(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setImports(imports);
      n.setModule(module);
      n.setPackageDeclaration(packageDeclaration);
      n.setTypes(types);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final ConditionalExpr n, final A arg) {
      Expression condition = (Expression)n.getCondition().accept(this, arg);
      Expression elseExpr = (Expression)n.getElseExpr().accept(this, arg);
      Expression thenExpr = (Expression)n.getThenExpr().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (condition != null && elseExpr != null && thenExpr != null) {
         n.setCondition(condition);
         n.setElseExpr(elseExpr);
         n.setThenExpr(thenExpr);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final ConstructorDeclaration n, final A arg) {
      BlockStmt body = (BlockStmt)n.getBody().accept((GenericVisitor)this, arg);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      NodeList<Parameter> parameters = this.modifyList(n.getParameters(), arg);
      ReceiverParameter receiverParameter = (ReceiverParameter)n.getReceiverParameter().map((s) -> {
         return (ReceiverParameter)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      NodeList<ReferenceType> thrownExceptions = this.modifyList(n.getThrownExceptions(), arg);
      NodeList<TypeParameter> typeParameters = this.modifyList(n.getTypeParameters(), arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body != null && name != null) {
         n.setBody(body);
         n.setName(name);
         n.setParameters(parameters);
         n.setReceiverParameter(receiverParameter);
         n.setThrownExceptions(thrownExceptions);
         n.setTypeParameters(typeParameters);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final ContinueStmt n, final A arg) {
      SimpleName label = (SimpleName)n.getLabel().map((s) -> {
         return (SimpleName)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setLabel(label);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final DoStmt n, final A arg) {
      Statement body = (Statement)n.getBody().accept(this, arg);
      Expression condition = (Expression)n.getCondition().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body != null && condition != null) {
         n.setBody(body);
         n.setCondition(condition);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final DoubleLiteralExpr n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final EmptyStmt n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final EnclosedExpr n, final A arg) {
      Expression inner = (Expression)n.getInner().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (inner == null) {
         return null;
      } else {
         n.setInner(inner);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final EnumConstantDeclaration n, final A arg) {
      NodeList<Expression> arguments = this.modifyList(n.getArguments(), arg);
      NodeList<BodyDeclaration<?>> classBody = this.modifyList(n.getClassBody(), arg);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setArguments(arguments);
         n.setClassBody(classBody);
         n.setName(name);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final EnumDeclaration n, final A arg) {
      NodeList<EnumConstantDeclaration> entries = this.modifyList(n.getEntries(), arg);
      NodeList<ClassOrInterfaceType> implementedTypes = this.modifyList(n.getImplementedTypes(), arg);
      NodeList<BodyDeclaration<?>> members = this.modifyList(n.getMembers(), arg);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setEntries(entries);
         n.setImplementedTypes(implementedTypes);
         n.setMembers(members);
         n.setName(name);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ExplicitConstructorInvocationStmt n, final A arg) {
      NodeList<Expression> arguments = this.modifyList(n.getArguments(), arg);
      Expression expression = (Expression)n.getExpression().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      NodeList<Type> typeArguments = this.modifyList(n.getTypeArguments(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setArguments(arguments);
      n.setExpression(expression);
      n.setTypeArguments(typeArguments);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final ExpressionStmt n, final A arg) {
      Expression expression = (Expression)n.getExpression().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (expression == null) {
         return null;
      } else {
         n.setExpression(expression);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final FieldAccessExpr n, final A arg) {
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      Expression scope = (Expression)n.getScope().accept(this, arg);
      NodeList<Type> typeArguments = this.modifyList(n.getTypeArguments(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name != null && scope != null) {
         n.setName(name);
         n.setScope(scope);
         n.setTypeArguments(typeArguments);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final FieldDeclaration n, final A arg) {
      NodeList<VariableDeclarator> variables = this.modifyList(n.getVariables(), arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (variables.isEmpty()) {
         return null;
      } else {
         n.setVariables(variables);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ForeachStmt n, final A arg) {
      Statement body = (Statement)n.getBody().accept(this, arg);
      Expression iterable = (Expression)n.getIterable().accept(this, arg);
      VariableDeclarationExpr variable = (VariableDeclarationExpr)n.getVariable().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body != null && iterable != null && variable != null) {
         n.setBody(body);
         n.setIterable(iterable);
         n.setVariable(variable);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final ForStmt n, final A arg) {
      Statement body = (Statement)n.getBody().accept(this, arg);
      Expression compare = (Expression)n.getCompare().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      NodeList<Expression> initialization = this.modifyList(n.getInitialization(), arg);
      NodeList<Expression> update = this.modifyList(n.getUpdate(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body == null) {
         return null;
      } else {
         n.setBody(body);
         n.setCompare(compare);
         n.setInitialization(initialization);
         n.setUpdate(update);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final IfStmt n, final A arg) {
      Expression condition = (Expression)n.getCondition().accept(this, arg);
      Statement elseStmt = (Statement)n.getElseStmt().map((s) -> {
         return (Statement)s.accept(this, arg);
      }).orElse((Object)null);
      Statement thenStmt = (Statement)n.getThenStmt().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (condition != null && thenStmt != null) {
         n.setCondition(condition);
         n.setElseStmt(elseStmt);
         n.setThenStmt(thenStmt);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final InitializerDeclaration n, final A arg) {
      BlockStmt body = (BlockStmt)n.getBody().accept((GenericVisitor)this, arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body == null) {
         return null;
      } else {
         n.setBody(body);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final InstanceOfExpr n, final A arg) {
      Expression expression = (Expression)n.getExpression().accept(this, arg);
      ReferenceType type = (ReferenceType)n.getType().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (expression != null && type != null) {
         n.setExpression(expression);
         n.setType(type);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final IntegerLiteralExpr n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final JavadocComment n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final LabeledStmt n, final A arg) {
      SimpleName label = (SimpleName)n.getLabel().accept((GenericVisitor)this, arg);
      Statement statement = (Statement)n.getStatement().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (label != null && statement != null) {
         n.setLabel(label);
         n.setStatement(statement);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final LongLiteralExpr n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final MarkerAnnotationExpr n, final A arg) {
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final MemberValuePair n, final A arg) {
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      Expression value = (Expression)n.getValue().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name != null && value != null) {
         n.setName(name);
         n.setValue(value);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final MethodCallExpr n, final A arg) {
      NodeList<Expression> arguments = this.modifyList(n.getArguments(), arg);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      Expression scope = (Expression)n.getScope().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      NodeList<Type> typeArguments = this.modifyList(n.getTypeArguments(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setArguments(arguments);
         n.setName(name);
         n.setScope(scope);
         n.setTypeArguments(typeArguments);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final MethodDeclaration n, final A arg) {
      BlockStmt body = (BlockStmt)n.getBody().map((s) -> {
         return (BlockStmt)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      Type type = (Type)n.getType().accept(this, arg);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      NodeList<Parameter> parameters = this.modifyList(n.getParameters(), arg);
      ReceiverParameter receiverParameter = (ReceiverParameter)n.getReceiverParameter().map((s) -> {
         return (ReceiverParameter)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      NodeList<ReferenceType> thrownExceptions = this.modifyList(n.getThrownExceptions(), arg);
      NodeList<TypeParameter> typeParameters = this.modifyList(n.getTypeParameters(), arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (type != null && name != null) {
         n.setBody(body);
         n.setType(type);
         n.setName(name);
         n.setParameters(parameters);
         n.setReceiverParameter(receiverParameter);
         n.setThrownExceptions(thrownExceptions);
         n.setTypeParameters(typeParameters);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final NameExpr n, final A arg) {
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final NormalAnnotationExpr n, final A arg) {
      NodeList<MemberValuePair> pairs = this.modifyList(n.getPairs(), arg);
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setPairs(pairs);
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final NullLiteralExpr n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final ObjectCreationExpr n, final A arg) {
      NodeList<BodyDeclaration<?>> anonymousClassBody = this.modifyList(n.getAnonymousClassBody(), arg);
      NodeList<Expression> arguments = this.modifyList(n.getArguments(), arg);
      Expression scope = (Expression)n.getScope().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      ClassOrInterfaceType type = (ClassOrInterfaceType)n.getType().accept((GenericVisitor)this, arg);
      NodeList<Type> typeArguments = this.modifyList(n.getTypeArguments(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (type == null) {
         return null;
      } else {
         n.setAnonymousClassBody(anonymousClassBody);
         n.setArguments(arguments);
         n.setScope(scope);
         n.setType(type);
         n.setTypeArguments(typeArguments);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final PackageDeclaration n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setAnnotations(annotations);
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final Parameter n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      Type type = (Type)n.getType().accept(this, arg);
      NodeList<AnnotationExpr> varArgsAnnotations = this.modifyList(n.getVarArgsAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name != null && type != null) {
         n.setAnnotations(annotations);
         n.setName(name);
         n.setType(type);
         n.setVarArgsAnnotations(varArgsAnnotations);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final Name n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Name qualifier = (Name)n.getQualifier().map((s) -> {
         return (Name)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setAnnotations(annotations);
      n.setQualifier(qualifier);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final PrimitiveType n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setAnnotations(annotations);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final SimpleName n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final ArrayType n, final A arg) {
      Type componentType = (Type)n.getComponentType().accept(this, arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (componentType == null) {
         return null;
      } else {
         n.setComponentType(componentType);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ArrayCreationLevel n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Expression dimension = (Expression)n.getDimension().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setAnnotations(annotations);
      n.setDimension(dimension);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final IntersectionType n, final A arg) {
      NodeList<ReferenceType> elements = this.modifyList(n.getElements(), arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (elements.isEmpty()) {
         return null;
      } else {
         n.setElements(elements);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final UnionType n, final A arg) {
      NodeList<ReferenceType> elements = this.modifyList(n.getElements(), arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (elements.isEmpty()) {
         return null;
      } else {
         n.setElements(elements);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ReturnStmt n, final A arg) {
      Expression expression = (Expression)n.getExpression().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setExpression(expression);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final SingleMemberAnnotationExpr n, final A arg) {
      Expression memberValue = (Expression)n.getMemberValue().accept(this, arg);
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (memberValue != null && name != null) {
         n.setMemberValue(memberValue);
         n.setName(name);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final StringLiteralExpr n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final SuperExpr n, final A arg) {
      Expression classExpr = (Expression)n.getClassExpr().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setClassExpr(classExpr);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final SwitchEntryStmt n, final A arg) {
      Expression label = (Expression)n.getLabel().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      NodeList<Statement> statements = this.modifyList(n.getStatements(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setLabel(label);
      n.setStatements(statements);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final SwitchStmt n, final A arg) {
      NodeList<SwitchEntryStmt> entries = this.modifyList(n.getEntries(), arg);
      Expression selector = (Expression)n.getSelector().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (selector == null) {
         return null;
      } else {
         n.setEntries(entries);
         n.setSelector(selector);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final SynchronizedStmt n, final A arg) {
      BlockStmt body = (BlockStmt)n.getBody().accept((GenericVisitor)this, arg);
      Expression expression = (Expression)n.getExpression().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body != null && expression != null) {
         n.setBody(body);
         n.setExpression(expression);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final ThisExpr n, final A arg) {
      Expression classExpr = (Expression)n.getClassExpr().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setClassExpr(classExpr);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final ThrowStmt n, final A arg) {
      Expression expression = (Expression)n.getExpression().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (expression == null) {
         return null;
      } else {
         n.setExpression(expression);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final TryStmt n, final A arg) {
      NodeList<CatchClause> catchClauses = this.modifyList(n.getCatchClauses(), arg);
      BlockStmt finallyBlock = (BlockStmt)n.getFinallyBlock().map((s) -> {
         return (BlockStmt)s.accept((GenericVisitor)this, arg);
      }).orElse((Object)null);
      NodeList<Expression> resources = this.modifyList(n.getResources(), arg);
      BlockStmt tryBlock = (BlockStmt)n.getTryBlock().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (tryBlock == null) {
         return null;
      } else {
         n.setCatchClauses(catchClauses);
         n.setFinallyBlock(finallyBlock);
         n.setResources(resources);
         n.setTryBlock(tryBlock);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final LocalClassDeclarationStmt n, final A arg) {
      ClassOrInterfaceDeclaration classDeclaration = (ClassOrInterfaceDeclaration)n.getClassDeclaration().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (classDeclaration == null) {
         return null;
      } else {
         n.setClassDeclaration(classDeclaration);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final TypeParameter n, final A arg) {
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      NodeList<ClassOrInterfaceType> typeBound = this.modifyList(n.getTypeBound(), arg);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setName(name);
         n.setTypeBound(typeBound);
         n.setAnnotations(annotations);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final UnaryExpr n, final A arg) {
      Expression expression = (Expression)n.getExpression().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (expression == null) {
         return null;
      } else {
         n.setExpression(expression);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final UnknownType n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setAnnotations(annotations);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final VariableDeclarationExpr n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      NodeList<VariableDeclarator> variables = this.modifyList(n.getVariables(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (variables.isEmpty()) {
         return null;
      } else {
         n.setAnnotations(annotations);
         n.setVariables(variables);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final VariableDeclarator n, final A arg) {
      Expression initializer = (Expression)n.getInitializer().map((s) -> {
         return (Expression)s.accept(this, arg);
      }).orElse((Object)null);
      SimpleName name = (SimpleName)n.getName().accept((GenericVisitor)this, arg);
      Type type = (Type)n.getType().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name != null && type != null) {
         n.setInitializer(initializer);
         n.setName(name);
         n.setType(type);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final VoidType n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setAnnotations(annotations);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final WhileStmt n, final A arg) {
      Statement body = (Statement)n.getBody().accept(this, arg);
      Expression condition = (Expression)n.getCondition().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body != null && condition != null) {
         n.setBody(body);
         n.setCondition(condition);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final WildcardType n, final A arg) {
      ReferenceType extendedType = (ReferenceType)n.getExtendedType().map((s) -> {
         return (ReferenceType)s.accept(this, arg);
      }).orElse((Object)null);
      ReferenceType superType = (ReferenceType)n.getSuperType().map((s) -> {
         return (ReferenceType)s.accept(this, arg);
      }).orElse((Object)null);
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setExtendedType(extendedType);
      n.setSuperType(superType);
      n.setAnnotations(annotations);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final LambdaExpr n, final A arg) {
      Statement body = (Statement)n.getBody().accept(this, arg);
      NodeList<Parameter> parameters = this.modifyList(n.getParameters(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (body == null) {
         return null;
      } else {
         n.setBody(body);
         n.setParameters(parameters);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final MethodReferenceExpr n, final A arg) {
      Expression scope = (Expression)n.getScope().accept(this, arg);
      NodeList<Type> typeArguments = this.modifyList(n.getTypeArguments(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (scope == null) {
         return null;
      } else {
         n.setScope(scope);
         n.setTypeArguments(typeArguments);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final TypeExpr n, final A arg) {
      Type type = (Type)n.getType().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (type == null) {
         return null;
      } else {
         n.setType(type);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(NodeList n, A arg) {
      if (n.isEmpty()) {
         return n;
      } else {
         List<Pair<Node, Node>> changeList = new ArrayList();
         List<Node> listCopy = new ArrayList(n);
         Iterator var5 = listCopy.iterator();

         while(var5.hasNext()) {
            Node node = (Node)var5.next();
            Node newNode = (Node)node.accept(this, arg);
            changeList.add(new Pair(node, newNode));
         }

         var5 = changeList.iterator();

         while(var5.hasNext()) {
            Pair<Node, Node> change = (Pair)var5.next();
            if (change.b == null) {
               n.remove((Node)change.a);
            } else {
               int i = n.indexOf(change.a);
               if (i != -1) {
                  n.set(i, (Node)change.b);
               }
            }
         }

         return n;
      }
   }

   public Node visit(final ImportDeclaration n, final A arg) {
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final BlockComment n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final LineComment n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   private <N extends Node> NodeList<N> modifyList(NodeList<N> list, A arg) {
      return (NodeList)list.accept((GenericVisitor)this, arg);
   }

   private <N extends Node> NodeList<N> modifyList(Optional<NodeList<N>> list, A arg) {
      return (NodeList)list.map((ns) -> {
         return this.modifyList(ns, arg);
      }).orElse((Object)null);
   }

   public Visitable visit(final ModuleDeclaration n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      NodeList<ModuleStmt> moduleStmts = this.modifyList(n.getModuleStmts(), arg);
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setAnnotations(annotations);
         n.setModuleStmts(moduleStmts);
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ModuleRequiresStmt n, final A arg) {
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ModuleExportsStmt n, final A arg) {
      NodeList<Name> moduleNames = this.modifyList(n.getModuleNames(), arg);
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setModuleNames(moduleNames);
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ModuleProvidesStmt n, final A arg) {
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      NodeList<Name> with = this.modifyList(n.getWith(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setName(name);
         n.setWith(with);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ModuleUsesStmt n, final A arg) {
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final ModuleOpensStmt n, final A arg) {
      NodeList<Name> moduleNames = this.modifyList(n.getModuleNames(), arg);
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name == null) {
         return null;
      } else {
         n.setModuleNames(moduleNames);
         n.setName(name);
         n.setComment(comment);
         return n;
      }
   }

   public Visitable visit(final UnparsableStmt n, final A arg) {
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setComment(comment);
      return n;
   }

   public Visitable visit(final ReceiverParameter n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Name name = (Name)n.getName().accept((GenericVisitor)this, arg);
      Type type = (Type)n.getType().accept(this, arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      if (name != null && type != null) {
         n.setAnnotations(annotations);
         n.setName(name);
         n.setType(type);
         n.setComment(comment);
         return n;
      } else {
         return null;
      }
   }

   public Visitable visit(final VarType n, final A arg) {
      NodeList<AnnotationExpr> annotations = this.modifyList(n.getAnnotations(), arg);
      Comment comment = (Comment)n.getComment().map((s) -> {
         return (Comment)s.accept(this, arg);
      }).orElse((Object)null);
      n.setAnnotations(annotations);
      n.setComment(comment);
      return n;
   }
}
