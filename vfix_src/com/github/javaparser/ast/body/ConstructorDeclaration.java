package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ConstructorDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;

public final class ConstructorDeclaration extends CallableDeclaration<ConstructorDeclaration> implements NodeWithBlockStmt<ConstructorDeclaration>, NodeWithAccessModifiers<ConstructorDeclaration>, NodeWithJavadoc<ConstructorDeclaration>, NodeWithSimpleName<ConstructorDeclaration>, NodeWithParameters<ConstructorDeclaration>, NodeWithThrownExceptions<ConstructorDeclaration>, NodeWithTypeParameters<ConstructorDeclaration>, Resolvable<ResolvedConstructorDeclaration> {
   private BlockStmt body;

   public ConstructorDeclaration() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new NodeList(), new SimpleName(), new NodeList(), new NodeList(), new BlockStmt(), (ReceiverParameter)null);
   }

   public ConstructorDeclaration(String name) {
      this((TokenRange)null, EnumSet.of(Modifier.PUBLIC), new NodeList(), new NodeList(), new SimpleName(name), new NodeList(), new NodeList(), new BlockStmt(), (ReceiverParameter)null);
   }

   public ConstructorDeclaration(EnumSet<Modifier> modifiers, String name) {
      this((TokenRange)null, modifiers, new NodeList(), new NodeList(), new SimpleName(name), new NodeList(), new NodeList(), new BlockStmt(), (ReceiverParameter)null);
   }

   public ConstructorDeclaration(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<TypeParameter> typeParameters, SimpleName name, NodeList<Parameter> parameters, NodeList<ReferenceType> thrownExceptions, BlockStmt body) {
      this((TokenRange)null, modifiers, annotations, typeParameters, name, parameters, thrownExceptions, body, (ReceiverParameter)null);
   }

   @AllFieldsConstructor
   public ConstructorDeclaration(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<TypeParameter> typeParameters, SimpleName name, NodeList<Parameter> parameters, NodeList<ReferenceType> thrownExceptions, BlockStmt body, ReceiverParameter receiverParameter) {
      this((TokenRange)null, modifiers, annotations, typeParameters, name, parameters, thrownExceptions, body, receiverParameter);
   }

   public ConstructorDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<TypeParameter> typeParameters, SimpleName name, NodeList<Parameter> parameters, NodeList<ReferenceType> thrownExceptions, BlockStmt body, ReceiverParameter receiverParameter) {
      super(tokenRange, modifiers, annotations, typeParameters, name, parameters, thrownExceptions, receiverParameter);
      this.setBody(body);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public BlockStmt getBody() {
      return this.body;
   }

   public ConstructorDeclaration setBody(final BlockStmt body) {
      Utils.assertNotNull(body);
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

   public ConstructorDeclaration setModifiers(final EnumSet<Modifier> modifiers) {
      return (ConstructorDeclaration)super.setModifiers(modifiers);
   }

   public ConstructorDeclaration setName(final SimpleName name) {
      return (ConstructorDeclaration)super.setName(name);
   }

   public ConstructorDeclaration setParameters(final NodeList<Parameter> parameters) {
      return (ConstructorDeclaration)super.setParameters(parameters);
   }

   public ConstructorDeclaration setThrownExceptions(final NodeList<ReferenceType> thrownExceptions) {
      return (ConstructorDeclaration)super.setThrownExceptions(thrownExceptions);
   }

   public ConstructorDeclaration setTypeParameters(final NodeList<TypeParameter> typeParameters) {
      return (ConstructorDeclaration)super.setTypeParameters(typeParameters);
   }

   public String getDeclarationAsString(boolean includingModifiers, boolean includingThrows, boolean includingParameterName) {
      StringBuilder sb = new StringBuilder();
      if (includingModifiers) {
         AccessSpecifier accessSpecifier = Modifier.getAccessSpecifier(this.getModifiers());
         sb.append(accessSpecifier.asString());
         sb.append(accessSpecifier == AccessSpecifier.DEFAULT ? "" : " ");
      }

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
         }
      }

      sb.append(")");
      sb.append(this.appendThrowsIfRequested(includingThrows));
      return sb.toString();
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ConstructorDeclaration clone() {
      return (ConstructorDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ConstructorDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.constructorDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.body) {
         this.setBody((BlockStmt)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isConstructorDeclaration() {
      return true;
   }

   public ConstructorDeclaration asConstructorDeclaration() {
      return this;
   }

   public void ifConstructorDeclaration(Consumer<ConstructorDeclaration> action) {
      action.accept(this);
   }

   public ResolvedConstructorDeclaration resolve() {
      return (ResolvedConstructorDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedConstructorDeclaration.class);
   }

   public Optional<ConstructorDeclaration> toConstructorDeclaration() {
      return Optional.of(this);
   }
}
