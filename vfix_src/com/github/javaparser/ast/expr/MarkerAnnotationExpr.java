package com.github.javaparser.ast.expr;

import com.github.javaparser.JavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.MarkerAnnotationExprMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class MarkerAnnotationExpr extends AnnotationExpr {
   public MarkerAnnotationExpr() {
      this((TokenRange)null, new Name());
   }

   public MarkerAnnotationExpr(final String name) {
      this((TokenRange)null, JavaParser.parseName(name));
   }

   @AllFieldsConstructor
   public MarkerAnnotationExpr(final Name name) {
      this((TokenRange)null, name);
   }

   public MarkerAnnotationExpr(TokenRange tokenRange, Name name) {
      super(tokenRange, name);
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

   public MarkerAnnotationExpr clone() {
      return (MarkerAnnotationExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public MarkerAnnotationExprMetaModel getMetaModel() {
      return JavaParserMetaModel.markerAnnotationExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isMarkerAnnotationExpr() {
      return true;
   }

   public MarkerAnnotationExpr asMarkerAnnotationExpr() {
      return this;
   }

   public void ifMarkerAnnotationExpr(Consumer<MarkerAnnotationExpr> action) {
      action.accept(this);
   }

   public Optional<MarkerAnnotationExpr> toMarkerAnnotationExpr() {
      return Optional.of(this);
   }
}
