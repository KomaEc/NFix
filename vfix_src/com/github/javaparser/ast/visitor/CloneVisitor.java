package com.github.javaparser.ast.visitor;

import com.github.javaparser.TokenRange;
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
import java.util.Iterator;
import java.util.Optional;

public class CloneVisitor implements GenericVisitor<Visitable, Object> {
   public Visitable visit(final CompilationUnit n, final Object arg) {
      NodeList<ImportDeclaration> imports = this.cloneList(n.getImports(), arg);
      ModuleDeclaration module = (ModuleDeclaration)this.cloneNode(n.getModule(), arg);
      PackageDeclaration packageDeclaration = (PackageDeclaration)this.cloneNode(n.getPackageDeclaration(), arg);
      NodeList<TypeDeclaration<?>> types = this.cloneList(n.getTypes(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      CompilationUnit r = new CompilationUnit((TokenRange)n.getTokenRange().orElse((Object)null), packageDeclaration, imports, types, module);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final PackageDeclaration n, final Object arg) {
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      PackageDeclaration r = new PackageDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), annotations, name);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final TypeParameter n, final Object arg) {
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      NodeList<ClassOrInterfaceType> typeBound = this.cloneList(n.getTypeBound(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      TypeParameter r = new TypeParameter((TokenRange)n.getTokenRange().orElse((Object)null), name, typeBound, annotations);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final LineComment n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      LineComment r = new LineComment((TokenRange)n.getTokenRange().orElse((Object)null), n.getContent());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final BlockComment n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      BlockComment r = new BlockComment((TokenRange)n.getTokenRange().orElse((Object)null), n.getContent());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ClassOrInterfaceDeclaration n, final Object arg) {
      NodeList<ClassOrInterfaceType> extendedTypes = this.cloneList(n.getExtendedTypes(), arg);
      NodeList<ClassOrInterfaceType> implementedTypes = this.cloneList(n.getImplementedTypes(), arg);
      NodeList<TypeParameter> typeParameters = this.cloneList(n.getTypeParameters(), arg);
      NodeList<BodyDeclaration<?>> members = this.cloneList(n.getMembers(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ClassOrInterfaceDeclaration r = new ClassOrInterfaceDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, n.isInterface(), name, typeParameters, extendedTypes, implementedTypes, members);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final EnumDeclaration n, final Object arg) {
      NodeList<EnumConstantDeclaration> entries = this.cloneList(n.getEntries(), arg);
      NodeList<ClassOrInterfaceType> implementedTypes = this.cloneList(n.getImplementedTypes(), arg);
      NodeList<BodyDeclaration<?>> members = this.cloneList(n.getMembers(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      EnumDeclaration r = new EnumDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, name, implementedTypes, entries, members);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final EnumConstantDeclaration n, final Object arg) {
      NodeList<Expression> arguments = this.cloneList(n.getArguments(), arg);
      NodeList<BodyDeclaration<?>> classBody = this.cloneList(n.getClassBody(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      EnumConstantDeclaration r = new EnumConstantDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), annotations, name, arguments, classBody);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final AnnotationDeclaration n, final Object arg) {
      NodeList<BodyDeclaration<?>> members = this.cloneList(n.getMembers(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      AnnotationDeclaration r = new AnnotationDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, name, members);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final AnnotationMemberDeclaration n, final Object arg) {
      Expression defaultValue = (Expression)this.cloneNode(n.getDefaultValue(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      Type type = (Type)this.cloneNode((Node)n.getType(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      AnnotationMemberDeclaration r = new AnnotationMemberDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, type, name, defaultValue);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final FieldDeclaration n, final Object arg) {
      NodeList<VariableDeclarator> variables = this.cloneList(n.getVariables(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      FieldDeclaration r = new FieldDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, variables);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final VariableDeclarator n, final Object arg) {
      Expression initializer = (Expression)this.cloneNode(n.getInitializer(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      Type type = (Type)this.cloneNode((Node)n.getType(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      VariableDeclarator r = new VariableDeclarator((TokenRange)n.getTokenRange().orElse((Object)null), type, name, initializer);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ConstructorDeclaration n, final Object arg) {
      BlockStmt body = (BlockStmt)this.cloneNode((Node)n.getBody(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      NodeList<Parameter> parameters = this.cloneList(n.getParameters(), arg);
      ReceiverParameter receiverParameter = (ReceiverParameter)this.cloneNode(n.getReceiverParameter(), arg);
      NodeList<ReferenceType> thrownExceptions = this.cloneList(n.getThrownExceptions(), arg);
      NodeList<TypeParameter> typeParameters = this.cloneList(n.getTypeParameters(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ConstructorDeclaration r = new ConstructorDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, typeParameters, name, parameters, thrownExceptions, body, receiverParameter);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final MethodDeclaration n, final Object arg) {
      BlockStmt body = (BlockStmt)this.cloneNode(n.getBody(), arg);
      Type type = (Type)this.cloneNode((Node)n.getType(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      NodeList<Parameter> parameters = this.cloneList(n.getParameters(), arg);
      ReceiverParameter receiverParameter = (ReceiverParameter)this.cloneNode(n.getReceiverParameter(), arg);
      NodeList<ReferenceType> thrownExceptions = this.cloneList(n.getThrownExceptions(), arg);
      NodeList<TypeParameter> typeParameters = this.cloneList(n.getTypeParameters(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      MethodDeclaration r = new MethodDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, typeParameters, type, name, parameters, thrownExceptions, body, receiverParameter);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final Parameter n, final Object arg) {
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      Type type = (Type)this.cloneNode((Node)n.getType(), arg);
      NodeList<AnnotationExpr> varArgsAnnotations = this.cloneList(n.getVarArgsAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      Parameter r = new Parameter((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, type, n.isVarArgs(), varArgsAnnotations, name);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final InitializerDeclaration n, final Object arg) {
      BlockStmt body = (BlockStmt)this.cloneNode((Node)n.getBody(), arg);
      this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      InitializerDeclaration r = new InitializerDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), n.isStatic(), body);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final JavadocComment n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      JavadocComment r = new JavadocComment((TokenRange)n.getTokenRange().orElse((Object)null), n.getContent());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ClassOrInterfaceType n, final Object arg) {
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      ClassOrInterfaceType scope = (ClassOrInterfaceType)this.cloneNode(n.getScope(), arg);
      NodeList<Type> typeArguments = this.cloneList((NodeList)n.getTypeArguments().orElse((Object)null), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ClassOrInterfaceType r = new ClassOrInterfaceType((TokenRange)n.getTokenRange().orElse((Object)null), scope, name, typeArguments, annotations);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final PrimitiveType n, final Object arg) {
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      PrimitiveType r = new PrimitiveType((TokenRange)n.getTokenRange().orElse((Object)null), n.getType(), annotations);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ArrayType n, final Object arg) {
      Type componentType = (Type)this.cloneNode((Node)n.getComponentType(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ArrayType r = new ArrayType((TokenRange)n.getTokenRange().orElse((Object)null), componentType, n.getOrigin(), annotations);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ArrayCreationLevel n, final Object arg) {
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Expression dimension = (Expression)this.cloneNode(n.getDimension(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ArrayCreationLevel r = new ArrayCreationLevel((TokenRange)n.getTokenRange().orElse((Object)null), dimension, annotations);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final IntersectionType n, final Object arg) {
      NodeList<ReferenceType> elements = this.cloneList(n.getElements(), arg);
      this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      IntersectionType r = new IntersectionType((TokenRange)n.getTokenRange().orElse((Object)null), elements);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final UnionType n, final Object arg) {
      NodeList<ReferenceType> elements = this.cloneList(n.getElements(), arg);
      this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      UnionType r = new UnionType((TokenRange)n.getTokenRange().orElse((Object)null), elements);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final VoidType n, final Object arg) {
      this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      VoidType r = new VoidType((TokenRange)n.getTokenRange().orElse((Object)null));
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final WildcardType n, final Object arg) {
      ReferenceType extendedType = (ReferenceType)this.cloneNode(n.getExtendedType(), arg);
      ReferenceType superType = (ReferenceType)this.cloneNode(n.getSuperType(), arg);
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      WildcardType r = new WildcardType((TokenRange)n.getTokenRange().orElse((Object)null), extendedType, superType, annotations);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final UnknownType n, final Object arg) {
      this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      UnknownType r = new UnknownType((TokenRange)n.getTokenRange().orElse((Object)null));
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ArrayAccessExpr n, final Object arg) {
      Expression index = (Expression)this.cloneNode((Node)n.getIndex(), arg);
      Expression name = (Expression)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ArrayAccessExpr r = new ArrayAccessExpr((TokenRange)n.getTokenRange().orElse((Object)null), name, index);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ArrayCreationExpr n, final Object arg) {
      Type elementType = (Type)this.cloneNode((Node)n.getElementType(), arg);
      ArrayInitializerExpr initializer = (ArrayInitializerExpr)this.cloneNode(n.getInitializer(), arg);
      NodeList<ArrayCreationLevel> levels = this.cloneList(n.getLevels(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ArrayCreationExpr r = new ArrayCreationExpr((TokenRange)n.getTokenRange().orElse((Object)null), elementType, levels, initializer);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ArrayInitializerExpr n, final Object arg) {
      NodeList<Expression> values = this.cloneList(n.getValues(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ArrayInitializerExpr r = new ArrayInitializerExpr((TokenRange)n.getTokenRange().orElse((Object)null), values);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final AssignExpr n, final Object arg) {
      Expression target = (Expression)this.cloneNode((Node)n.getTarget(), arg);
      Expression value = (Expression)this.cloneNode((Node)n.getValue(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      AssignExpr r = new AssignExpr((TokenRange)n.getTokenRange().orElse((Object)null), target, value, n.getOperator());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final BinaryExpr n, final Object arg) {
      Expression left = (Expression)this.cloneNode((Node)n.getLeft(), arg);
      Expression right = (Expression)this.cloneNode((Node)n.getRight(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      BinaryExpr r = new BinaryExpr((TokenRange)n.getTokenRange().orElse((Object)null), left, right, n.getOperator());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final CastExpr n, final Object arg) {
      Expression expression = (Expression)this.cloneNode((Node)n.getExpression(), arg);
      Type type = (Type)this.cloneNode((Node)n.getType(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      CastExpr r = new CastExpr((TokenRange)n.getTokenRange().orElse((Object)null), type, expression);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ClassExpr n, final Object arg) {
      Type type = (Type)this.cloneNode((Node)n.getType(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ClassExpr r = new ClassExpr((TokenRange)n.getTokenRange().orElse((Object)null), type);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ConditionalExpr n, final Object arg) {
      Expression condition = (Expression)this.cloneNode((Node)n.getCondition(), arg);
      Expression elseExpr = (Expression)this.cloneNode((Node)n.getElseExpr(), arg);
      Expression thenExpr = (Expression)this.cloneNode((Node)n.getThenExpr(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ConditionalExpr r = new ConditionalExpr((TokenRange)n.getTokenRange().orElse((Object)null), condition, thenExpr, elseExpr);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final EnclosedExpr n, final Object arg) {
      Expression inner = (Expression)this.cloneNode((Node)n.getInner(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      EnclosedExpr r = new EnclosedExpr((TokenRange)n.getTokenRange().orElse((Object)null), inner);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final FieldAccessExpr n, final Object arg) {
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      Expression scope = (Expression)this.cloneNode((Node)n.getScope(), arg);
      NodeList<Type> typeArguments = this.cloneList((NodeList)n.getTypeArguments().orElse((Object)null), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      FieldAccessExpr r = new FieldAccessExpr((TokenRange)n.getTokenRange().orElse((Object)null), scope, typeArguments, name);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final InstanceOfExpr n, final Object arg) {
      Expression expression = (Expression)this.cloneNode((Node)n.getExpression(), arg);
      ReferenceType type = (ReferenceType)this.cloneNode((Node)n.getType(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      InstanceOfExpr r = new InstanceOfExpr((TokenRange)n.getTokenRange().orElse((Object)null), expression, type);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final StringLiteralExpr n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      StringLiteralExpr r = new StringLiteralExpr((TokenRange)n.getTokenRange().orElse((Object)null), n.getValue());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final IntegerLiteralExpr n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      IntegerLiteralExpr r = new IntegerLiteralExpr((TokenRange)n.getTokenRange().orElse((Object)null), n.getValue());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final LongLiteralExpr n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      LongLiteralExpr r = new LongLiteralExpr((TokenRange)n.getTokenRange().orElse((Object)null), n.getValue());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final CharLiteralExpr n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      CharLiteralExpr r = new CharLiteralExpr((TokenRange)n.getTokenRange().orElse((Object)null), n.getValue());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final DoubleLiteralExpr n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      DoubleLiteralExpr r = new DoubleLiteralExpr((TokenRange)n.getTokenRange().orElse((Object)null), n.getValue());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final BooleanLiteralExpr n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      BooleanLiteralExpr r = new BooleanLiteralExpr((TokenRange)n.getTokenRange().orElse((Object)null), n.getValue());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final NullLiteralExpr n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      NullLiteralExpr r = new NullLiteralExpr((TokenRange)n.getTokenRange().orElse((Object)null));
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final MethodCallExpr n, final Object arg) {
      NodeList<Expression> arguments = this.cloneList(n.getArguments(), arg);
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      Expression scope = (Expression)this.cloneNode(n.getScope(), arg);
      NodeList<Type> typeArguments = this.cloneList((NodeList)n.getTypeArguments().orElse((Object)null), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      MethodCallExpr r = new MethodCallExpr((TokenRange)n.getTokenRange().orElse((Object)null), scope, typeArguments, name, arguments);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final NameExpr n, final Object arg) {
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      NameExpr r = new NameExpr((TokenRange)n.getTokenRange().orElse((Object)null), name);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ObjectCreationExpr n, final Object arg) {
      NodeList<BodyDeclaration<?>> anonymousClassBody = this.cloneList((NodeList)n.getAnonymousClassBody().orElse((Object)null), arg);
      NodeList<Expression> arguments = this.cloneList(n.getArguments(), arg);
      Expression scope = (Expression)this.cloneNode(n.getScope(), arg);
      ClassOrInterfaceType type = (ClassOrInterfaceType)this.cloneNode((Node)n.getType(), arg);
      NodeList<Type> typeArguments = this.cloneList((NodeList)n.getTypeArguments().orElse((Object)null), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ObjectCreationExpr r = new ObjectCreationExpr((TokenRange)n.getTokenRange().orElse((Object)null), scope, type, typeArguments, arguments, anonymousClassBody);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final Name n, final Object arg) {
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Name qualifier = (Name)this.cloneNode(n.getQualifier(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      Name r = new Name((TokenRange)n.getTokenRange().orElse((Object)null), qualifier, n.getIdentifier(), annotations);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final SimpleName n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      SimpleName r = new SimpleName((TokenRange)n.getTokenRange().orElse((Object)null), n.getIdentifier());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ThisExpr n, final Object arg) {
      Expression classExpr = (Expression)this.cloneNode(n.getClassExpr(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ThisExpr r = new ThisExpr((TokenRange)n.getTokenRange().orElse((Object)null), classExpr);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final SuperExpr n, final Object arg) {
      Expression classExpr = (Expression)this.cloneNode(n.getClassExpr(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      SuperExpr r = new SuperExpr((TokenRange)n.getTokenRange().orElse((Object)null), classExpr);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final UnaryExpr n, final Object arg) {
      Expression expression = (Expression)this.cloneNode((Node)n.getExpression(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      UnaryExpr r = new UnaryExpr((TokenRange)n.getTokenRange().orElse((Object)null), expression, n.getOperator());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final VariableDeclarationExpr n, final Object arg) {
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      NodeList<VariableDeclarator> variables = this.cloneList(n.getVariables(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      VariableDeclarationExpr r = new VariableDeclarationExpr((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), annotations, variables);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final MarkerAnnotationExpr n, final Object arg) {
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      MarkerAnnotationExpr r = new MarkerAnnotationExpr((TokenRange)n.getTokenRange().orElse((Object)null), name);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final SingleMemberAnnotationExpr n, final Object arg) {
      Expression memberValue = (Expression)this.cloneNode((Node)n.getMemberValue(), arg);
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      SingleMemberAnnotationExpr r = new SingleMemberAnnotationExpr((TokenRange)n.getTokenRange().orElse((Object)null), name, memberValue);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final NormalAnnotationExpr n, final Object arg) {
      NodeList<MemberValuePair> pairs = this.cloneList(n.getPairs(), arg);
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      NormalAnnotationExpr r = new NormalAnnotationExpr((TokenRange)n.getTokenRange().orElse((Object)null), name, pairs);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final MemberValuePair n, final Object arg) {
      SimpleName name = (SimpleName)this.cloneNode((Node)n.getName(), arg);
      Expression value = (Expression)this.cloneNode((Node)n.getValue(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      MemberValuePair r = new MemberValuePair((TokenRange)n.getTokenRange().orElse((Object)null), name, value);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ExplicitConstructorInvocationStmt n, final Object arg) {
      NodeList<Expression> arguments = this.cloneList(n.getArguments(), arg);
      Expression expression = (Expression)this.cloneNode(n.getExpression(), arg);
      NodeList<Type> typeArguments = this.cloneList((NodeList)n.getTypeArguments().orElse((Object)null), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ExplicitConstructorInvocationStmt r = new ExplicitConstructorInvocationStmt((TokenRange)n.getTokenRange().orElse((Object)null), typeArguments, n.isThis(), expression, arguments);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final LocalClassDeclarationStmt n, final Object arg) {
      ClassOrInterfaceDeclaration classDeclaration = (ClassOrInterfaceDeclaration)this.cloneNode((Node)n.getClassDeclaration(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      LocalClassDeclarationStmt r = new LocalClassDeclarationStmt((TokenRange)n.getTokenRange().orElse((Object)null), classDeclaration);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final AssertStmt n, final Object arg) {
      Expression check = (Expression)this.cloneNode((Node)n.getCheck(), arg);
      Expression message = (Expression)this.cloneNode(n.getMessage(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      AssertStmt r = new AssertStmt((TokenRange)n.getTokenRange().orElse((Object)null), check, message);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final BlockStmt n, final Object arg) {
      NodeList<Statement> statements = this.cloneList(n.getStatements(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      BlockStmt r = new BlockStmt((TokenRange)n.getTokenRange().orElse((Object)null), statements);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final LabeledStmt n, final Object arg) {
      SimpleName label = (SimpleName)this.cloneNode((Node)n.getLabel(), arg);
      Statement statement = (Statement)this.cloneNode((Node)n.getStatement(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      LabeledStmt r = new LabeledStmt((TokenRange)n.getTokenRange().orElse((Object)null), label, statement);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final EmptyStmt n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      EmptyStmt r = new EmptyStmt((TokenRange)n.getTokenRange().orElse((Object)null));
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ExpressionStmt n, final Object arg) {
      Expression expression = (Expression)this.cloneNode((Node)n.getExpression(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ExpressionStmt r = new ExpressionStmt((TokenRange)n.getTokenRange().orElse((Object)null), expression);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final SwitchStmt n, final Object arg) {
      NodeList<SwitchEntryStmt> entries = this.cloneList(n.getEntries(), arg);
      Expression selector = (Expression)this.cloneNode((Node)n.getSelector(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      SwitchStmt r = new SwitchStmt((TokenRange)n.getTokenRange().orElse((Object)null), selector, entries);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final SwitchEntryStmt n, final Object arg) {
      Expression label = (Expression)this.cloneNode(n.getLabel(), arg);
      NodeList<Statement> statements = this.cloneList(n.getStatements(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      SwitchEntryStmt r = new SwitchEntryStmt((TokenRange)n.getTokenRange().orElse((Object)null), label, statements);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final BreakStmt n, final Object arg) {
      SimpleName label = (SimpleName)this.cloneNode(n.getLabel(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      BreakStmt r = new BreakStmt((TokenRange)n.getTokenRange().orElse((Object)null), label);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ReturnStmt n, final Object arg) {
      Expression expression = (Expression)this.cloneNode(n.getExpression(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ReturnStmt r = new ReturnStmt((TokenRange)n.getTokenRange().orElse((Object)null), expression);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final IfStmt n, final Object arg) {
      Expression condition = (Expression)this.cloneNode((Node)n.getCondition(), arg);
      Statement elseStmt = (Statement)this.cloneNode(n.getElseStmt(), arg);
      Statement thenStmt = (Statement)this.cloneNode((Node)n.getThenStmt(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      IfStmt r = new IfStmt((TokenRange)n.getTokenRange().orElse((Object)null), condition, thenStmt, elseStmt);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final WhileStmt n, final Object arg) {
      Statement body = (Statement)this.cloneNode((Node)n.getBody(), arg);
      Expression condition = (Expression)this.cloneNode((Node)n.getCondition(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      WhileStmt r = new WhileStmt((TokenRange)n.getTokenRange().orElse((Object)null), condition, body);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ContinueStmt n, final Object arg) {
      SimpleName label = (SimpleName)this.cloneNode(n.getLabel(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ContinueStmt r = new ContinueStmt((TokenRange)n.getTokenRange().orElse((Object)null), label);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final DoStmt n, final Object arg) {
      Statement body = (Statement)this.cloneNode((Node)n.getBody(), arg);
      Expression condition = (Expression)this.cloneNode((Node)n.getCondition(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      DoStmt r = new DoStmt((TokenRange)n.getTokenRange().orElse((Object)null), body, condition);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ForeachStmt n, final Object arg) {
      Statement body = (Statement)this.cloneNode((Node)n.getBody(), arg);
      Expression iterable = (Expression)this.cloneNode((Node)n.getIterable(), arg);
      VariableDeclarationExpr variable = (VariableDeclarationExpr)this.cloneNode((Node)n.getVariable(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ForeachStmt r = new ForeachStmt((TokenRange)n.getTokenRange().orElse((Object)null), variable, iterable, body);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ForStmt n, final Object arg) {
      Statement body = (Statement)this.cloneNode((Node)n.getBody(), arg);
      Expression compare = (Expression)this.cloneNode(n.getCompare(), arg);
      NodeList<Expression> initialization = this.cloneList(n.getInitialization(), arg);
      NodeList<Expression> update = this.cloneList(n.getUpdate(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ForStmt r = new ForStmt((TokenRange)n.getTokenRange().orElse((Object)null), initialization, compare, update, body);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ThrowStmt n, final Object arg) {
      Expression expression = (Expression)this.cloneNode((Node)n.getExpression(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ThrowStmt r = new ThrowStmt((TokenRange)n.getTokenRange().orElse((Object)null), expression);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final SynchronizedStmt n, final Object arg) {
      BlockStmt body = (BlockStmt)this.cloneNode((Node)n.getBody(), arg);
      Expression expression = (Expression)this.cloneNode((Node)n.getExpression(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      SynchronizedStmt r = new SynchronizedStmt((TokenRange)n.getTokenRange().orElse((Object)null), expression, body);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final TryStmt n, final Object arg) {
      NodeList<CatchClause> catchClauses = this.cloneList(n.getCatchClauses(), arg);
      BlockStmt finallyBlock = (BlockStmt)this.cloneNode(n.getFinallyBlock(), arg);
      NodeList<Expression> resources = this.cloneList(n.getResources(), arg);
      BlockStmt tryBlock = (BlockStmt)this.cloneNode((Node)n.getTryBlock(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      TryStmt r = new TryStmt((TokenRange)n.getTokenRange().orElse((Object)null), resources, tryBlock, catchClauses, finallyBlock);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final CatchClause n, final Object arg) {
      BlockStmt body = (BlockStmt)this.cloneNode((Node)n.getBody(), arg);
      Parameter parameter = (Parameter)this.cloneNode((Node)n.getParameter(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      CatchClause r = new CatchClause((TokenRange)n.getTokenRange().orElse((Object)null), parameter, body);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final LambdaExpr n, final Object arg) {
      Statement body = (Statement)this.cloneNode((Node)n.getBody(), arg);
      NodeList<Parameter> parameters = this.cloneList(n.getParameters(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      LambdaExpr r = new LambdaExpr((TokenRange)n.getTokenRange().orElse((Object)null), parameters, body, n.isEnclosingParameters());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final MethodReferenceExpr n, final Object arg) {
      Expression scope = (Expression)this.cloneNode((Node)n.getScope(), arg);
      NodeList<Type> typeArguments = this.cloneList((NodeList)n.getTypeArguments().orElse((Object)null), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      MethodReferenceExpr r = new MethodReferenceExpr((TokenRange)n.getTokenRange().orElse((Object)null), scope, typeArguments, n.getIdentifier());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final TypeExpr n, final Object arg) {
      Type type = (Type)this.cloneNode((Node)n.getType(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      TypeExpr r = new TypeExpr((TokenRange)n.getTokenRange().orElse((Object)null), type);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(NodeList n, Object arg) {
      NodeList<Node> newNodes = new NodeList();
      Iterator var4 = n.iterator();

      while(var4.hasNext()) {
         Object node = var4.next();
         Node resultNode = (Node)((Node)node).accept(this, arg);
         if (resultNode != null) {
            newNodes.add(resultNode);
         }
      }

      return newNodes;
   }

   public Node visit(final ImportDeclaration n, final Object arg) {
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ImportDeclaration r = new ImportDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), name, n.isStatic(), n.isAsterisk());
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ModuleDeclaration n, final Object arg) {
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      NodeList<ModuleStmt> moduleStmts = this.cloneList(n.getModuleStmts(), arg);
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ModuleDeclaration r = new ModuleDeclaration((TokenRange)n.getTokenRange().orElse((Object)null), annotations, name, n.isOpen(), moduleStmts);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ModuleRequiresStmt n, final Object arg) {
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ModuleRequiresStmt r = new ModuleRequiresStmt((TokenRange)n.getTokenRange().orElse((Object)null), n.getModifiers(), name);
      r.setComment(comment);
      return r;
   }

   protected <T extends Node> T cloneNode(Optional<T> node, Object arg) {
      if (!node.isPresent()) {
         return null;
      } else {
         Node r = (Node)((Node)node.get()).accept(this, arg);
         return r == null ? null : r;
      }
   }

   protected <T extends Node> T cloneNode(T node, Object arg) {
      if (node == null) {
         return null;
      } else {
         Node r = (Node)node.accept(this, arg);
         return r == null ? null : r;
      }
   }

   private <N extends Node> NodeList<N> cloneList(NodeList<N> list, Object arg) {
      return list == null ? null : (NodeList)list.accept((GenericVisitor)this, arg);
   }

   public Visitable visit(final ModuleExportsStmt n, final Object arg) {
      NodeList<Name> moduleNames = this.cloneList(n.getModuleNames(), arg);
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ModuleExportsStmt r = new ModuleExportsStmt((TokenRange)n.getTokenRange().orElse((Object)null), name, moduleNames);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ModuleProvidesStmt n, final Object arg) {
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      NodeList<Name> with = this.cloneList(n.getWith(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ModuleProvidesStmt r = new ModuleProvidesStmt((TokenRange)n.getTokenRange().orElse((Object)null), name, with);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ModuleUsesStmt n, final Object arg) {
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ModuleUsesStmt r = new ModuleUsesStmt((TokenRange)n.getTokenRange().orElse((Object)null), name);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ModuleOpensStmt n, final Object arg) {
      NodeList<Name> moduleNames = this.cloneList(n.getModuleNames(), arg);
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ModuleOpensStmt r = new ModuleOpensStmt((TokenRange)n.getTokenRange().orElse((Object)null), name, moduleNames);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final UnparsableStmt n, final Object arg) {
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      UnparsableStmt r = new UnparsableStmt((TokenRange)n.getTokenRange().orElse((Object)null));
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final ReceiverParameter n, final Object arg) {
      NodeList<AnnotationExpr> annotations = this.cloneList(n.getAnnotations(), arg);
      Name name = (Name)this.cloneNode((Node)n.getName(), arg);
      Type type = (Type)this.cloneNode((Node)n.getType(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      ReceiverParameter r = new ReceiverParameter((TokenRange)n.getTokenRange().orElse((Object)null), annotations, type, name);
      r.setComment(comment);
      return r;
   }

   public Visitable visit(final VarType n, final Object arg) {
      this.cloneList(n.getAnnotations(), arg);
      Comment comment = (Comment)this.cloneNode(n.getComment(), arg);
      VarType r = new VarType((TokenRange)n.getTokenRange().orElse((Object)null));
      r.setComment(comment);
      return r;
   }
}
