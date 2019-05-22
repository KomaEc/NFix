package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.AnnotationDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

public final class AnnotationDeclaration extends TypeDeclaration<AnnotationDeclaration> implements NodeWithAbstractModifier<AnnotationDeclaration>, Resolvable<ResolvedAnnotationDeclaration> {
   public AnnotationDeclaration() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new SimpleName(), new NodeList());
   }

   public AnnotationDeclaration(EnumSet<Modifier> modifiers, String name) {
      this((TokenRange)null, modifiers, new NodeList(), new SimpleName(name), new NodeList());
   }

   @AllFieldsConstructor
   public AnnotationDeclaration(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, SimpleName name, NodeList<BodyDeclaration<?>> members) {
      this((TokenRange)null, modifiers, annotations, name, members);
   }

   public AnnotationDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, SimpleName name, NodeList<BodyDeclaration<?>> members) {
      super(tokenRange, modifiers, annotations, name, members);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public AnnotationDeclaration clone() {
      return (AnnotationDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public AnnotationDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.annotationDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isAnnotationDeclaration() {
      return true;
   }

   public AnnotationDeclaration asAnnotationDeclaration() {
      return this;
   }

   public void ifAnnotationDeclaration(Consumer<AnnotationDeclaration> action) {
      action.accept(this);
   }

   public ResolvedAnnotationDeclaration resolve() {
      return (ResolvedAnnotationDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedAnnotationDeclaration.class);
   }

   public Optional<AnnotationDeclaration> toAnnotationDeclaration() {
      return Optional.of(this);
   }
}
