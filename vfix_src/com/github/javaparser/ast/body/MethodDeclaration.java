package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithFinalModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStrictfpModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.MethodDeclarationMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;

public final class MethodDeclaration extends CallableDeclaration<MethodDeclaration> implements NodeWithType<MethodDeclaration, Type>, NodeWithOptionalBlockStmt<MethodDeclaration>, NodeWithJavadoc<MethodDeclaration>, NodeWithDeclaration, NodeWithSimpleName<MethodDeclaration>, NodeWithParameters<MethodDeclaration>, NodeWithThrownExceptions<MethodDeclaration>, NodeWithTypeParameters<MethodDeclaration>, NodeWithAccessModifiers<MethodDeclaration>, NodeWithAbstractModifier<MethodDeclaration>, NodeWithStaticModifier<MethodDeclaration>, NodeWithFinalModifier<MethodDeclaration>, NodeWithStrictfpModifier<MethodDeclaration>, Resolvable<ResolvedMethodDeclaration> {
   private Type type;
   @OptionalProperty
   private BlockStmt body;

   public MethodDeclaration() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new NodeList(), new ClassOrInterfaceType(), new SimpleName(), new NodeList(), new NodeList(), new BlockStmt(), (ReceiverParameter)null);
   }

   public MethodDeclaration(final EnumSet<Modifier> modifiers, final Type type, final String name) {
      this((TokenRange)null, modifiers, new NodeList(), new NodeList(), type, new SimpleName(name), new NodeList(), new NodeList(), new BlockStmt(), (ReceiverParameter)null);
   }

   public MethodDeclaration(final EnumSet<Modifier> modifiers, final String name, final Type type, final NodeList<Parameter> parameters) {
      this((TokenRange)null, modifiers, new NodeList(), new NodeList(), type, new SimpleName(name), parameters, new NodeList(), new BlockStmt(), (ReceiverParameter)null);
   }

   public MethodDeclaration(final EnumSet<Modifier> modifiers, final NodeList<AnnotationExpr> annotations, final NodeList<TypeParameter> typeParameters, final Type type, final SimpleName name, final NodeList<Parameter> parameters, final NodeList<ReferenceType> thrownExceptions, final BlockStmt body) {
      this((TokenRange)null, modifiers, annotations, typeParameters, type, name, parameters, thrownExceptions, body, (ReceiverParameter)null);
   }

   @AllFieldsConstructor
   public MethodDeclaration(final EnumSet<Modifier> modifiers, final NodeList<AnnotationExpr> annotations, final NodeList<TypeParameter> typeParameters, final Type type, final SimpleName name, final NodeList<Parameter> parameters, final NodeList<ReferenceType> thrownExceptions, final BlockStmt body, ReceiverParameter receiverParameter) {
      this((TokenRange)null, modifiers, annotations, typeParameters, type, name, parameters, thrownExceptions, body, receiverParameter);
   }

   /** @deprecated */
   @Deprecated
   public MethodDeclaration(final EnumSet<Modifier> modifiers, final NodeList<AnnotationExpr> annotations, final NodeList<TypeParameter> typeParameters, final Type type, final SimpleName name, final boolean isDefault, final NodeList<Parameter> parameters, final NodeList<ReferenceType> thrownExceptions, final BlockStmt body) {
      this((TokenRange)null, modifiers, annotations, typeParameters, type, name, parameters, thrownExceptions, body, (ReceiverParameter)null);
      this.setDefault(isDefault);
   }

   public MethodDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<TypeParameter> typeParameters, Type type, SimpleName name, NodeList<Parameter> parameters, NodeList<ReferenceType> thrownExceptions, BlockStmt body, ReceiverParameter receiverParameter) {
      super(tokenRange, modifiers, annotations, typeParameters, name, parameters, thrownExceptions, receiverParameter);
      this.setType(type);
      this.setBody(body);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<BlockStmt> getBody() {
      return Optional.ofNullable(this.body);
   }

   public MethodDeclaration setBody(final BlockStmt body) {
      if (body == this.body) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.BODY, this.body, body);
         if (this.body != null) {
            this.body.setParentNode((Node)null);
         }

         this.body = body;
         this.setAsParentNodeOf(body);
         return this;
      }
   }

   public Type getType() {
      return this.type;
   }

   public MethodDeclaration setType(final Type type) {
      Utils.assertNotNull(type);
      if (type == this.type) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE, this.type, type);
         if (this.type != null) {
            this.type.setParentNode((Node)null);
         }

         this.type = type;
         this.setAsParentNodeOf(type);
         return this;
      }
   }

   public MethodDeclaration setModifiers(final EnumSet<Modifier> modifiers) {
      return (MethodDeclaration)super.setModifiers(modifiers);
   }

   public MethodDeclaration setName(final SimpleName name) {
      return (MethodDeclaration)super.setName(name);
   }

   public MethodDeclaration setParameters(final NodeList<Parameter> parameters) {
      return (MethodDeclaration)super.setParameters(parameters);
   }

   public MethodDeclaration setThrownExceptions(final NodeList<ReferenceType> thrownExceptions) {
      return (MethodDeclaration)super.setThrownExceptions(thrownExceptions);
   }

   public MethodDeclaration setTypeParameters(final NodeList<TypeParameter> typeParameters) {
      return (MethodDeclaration)super.setTypeParameters(typeParameters);
   }

   public String getDeclarationAsString(boolean includingModifiers, boolean includingThrows, boolean includingParameterName) {
      StringBuilder sb = new StringBuilder();
      if (includingModifiers) {
         AccessSpecifier accessSpecifier = Modifier.getAccessSpecifier(this.getModifiers());
         sb.append(accessSpecifier.asString());
         sb.append(accessSpecifier == AccessSpecifier.DEFAULT ? "" : " ");
         if (this.getModifiers().contains(Modifier.STATIC)) {
            sb.append("static ");
         }

         if (this.getModifiers().contains(Modifier.ABSTRACT)) {
            sb.append("abstract ");
         }

         if (this.getModifiers().contains(Modifier.FINAL)) {
            sb.append("final ");
         }

         if (this.getModifiers().contains(Modifier.NATIVE)) {
            sb.append("native ");
         }

         if (this.getModifiers().contains(Modifier.SYNCHRONIZED)) {
            sb.append("synchronized ");
         }
      }

      sb.append(this.getType().toString(prettyPrinterNoCommentsConfiguration));
      sb.append(" ");
      sb.append(this.getName());
      sb.append("(");
      boolean firstParam = true;
      Iterator var6 = this.getParameters().iterator();

      while(var6.hasNext()) {
         Parameter param = (Parameter)var6.next();
         if (firstParam) {
            firstParam = false;
         } else {
            sb.append(", ");
         }

         if (includingParameterName) {
            sb.append(param.toString(prettyPrinterNoCommentsConfiguration));
         } else {
            sb.append(param.getType().toString(prettyPrinterNoCommentsConfiguration));
            if (param.isVarArgs()) {
               sb.append("...");
            }
         }
      }

      sb.append(")");
      sb.append(this.appendThrowsIfRequested(includingThrows));
      return sb.toString();
   }

   public boolean isNative() {
      return this.getModifiers().contains(Modifier.NATIVE);
   }

   public boolean isSynchronized() {
      return this.getModifiers().contains(Modifier.SYNCHRONIZED);
   }

   public boolean isDefault() {
      return this.getModifiers().contains(Modifier.DEFAULT);
   }

   public MethodDeclaration setNative(boolean set) {
      return (MethodDeclaration)this.setModifier(Modifier.NATIVE, set);
   }

   public MethodDeclaration setSynchronized(boolean set) {
      return (MethodDeclaration)this.setModifier(Modifier.SYNCHRONIZED, set);
   }

   public MethodDeclaration setDefault(boolean set) {
      return (MethodDeclaration)this.setModifier(Modifier.DEFAULT, set);
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.body != null && node == this.body) {
         this.removeBody();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public MethodDeclaration removeBody() {
      return this.setBody((BlockStmt)null);
   }

   public MethodDeclaration clone() {
      return (MethodDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public MethodDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.methodDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.body != null && node == this.body) {
         this.setBody((BlockStmt)replacementNode);
         return true;
      } else if (node == this.type) {
         this.setType((Type)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isMethodDeclaration() {
      return true;
   }

   public MethodDeclaration asMethodDeclaration() {
      return this;
   }

   public void ifMethodDeclaration(Consumer<MethodDeclaration> action) {
      action.accept(this);
   }

   public ResolvedMethodDeclaration resolve() {
      return (ResolvedMethodDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedMethodDeclaration.class);
   }

   public Optional<MethodDeclaration> toMethodDeclaration() {
      return Optional.of(this);
   }
}
