package com.github.javaparser.ast.type;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.VoidTypeMetaModel;
import com.github.javaparser.resolution.types.ResolvedVoidType;
import java.util.Optional;
import java.util.function.Consumer;

public final class VoidType extends Type implements NodeWithAnnotations<VoidType> {
   @AllFieldsConstructor
   public VoidType() {
      this((TokenRange)null);
   }

   public VoidType(TokenRange tokenRange) {
      super(tokenRange);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public VoidType setAnnotations(NodeList<AnnotationExpr> annotations) {
      return (VoidType)super.setAnnotations(annotations);
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public String asString() {
      return "void";
   }

   public VoidType clone() {
      return (VoidType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public VoidTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.voidTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isVoidType() {
      return true;
   }

   public VoidType asVoidType() {
      return this;
   }

   public void ifVoidType(Consumer<VoidType> action) {
      action.accept(this);
   }

   public ResolvedVoidType resolve() {
      return (ResolvedVoidType)this.getSymbolResolver().toResolvedType(this, ResolvedVoidType.class);
   }

   public Optional<VoidType> toVoidType() {
      return Optional.of(this);
   }
}
