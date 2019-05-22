package com.github.javaparser.ast.type;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.UnknownTypeMetaModel;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.Optional;
import java.util.function.Consumer;

public final class UnknownType extends Type {
   @AllFieldsConstructor
   public UnknownType() {
      this((TokenRange)null);
   }

   public UnknownType(TokenRange tokenRange) {
      super(tokenRange);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public UnknownType setAnnotations(NodeList<AnnotationExpr> annotations) {
      if (annotations.size() > 0) {
         throw new IllegalStateException("Inferred lambda types cannot be annotated.");
      } else {
         return (UnknownType)super.setAnnotations(annotations);
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public String asString() {
      return "";
   }

   public UnknownType clone() {
      return (UnknownType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public UnknownTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.unknownTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isUnknownType() {
      return true;
   }

   public UnknownType asUnknownType() {
      return this;
   }

   public void ifUnknownType(Consumer<UnknownType> action) {
      action.accept(this);
   }

   public ResolvedType resolve() {
      return (ResolvedType)this.getSymbolResolver().toResolvedType(this, ResolvedReferenceType.class);
   }

   public Optional<UnknownType> toUnknownType() {
      return Optional.of(this);
   }
}
