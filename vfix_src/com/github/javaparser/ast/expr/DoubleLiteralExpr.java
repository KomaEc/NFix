package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.DoubleLiteralExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class DoubleLiteralExpr extends LiteralStringValueExpr {
   public DoubleLiteralExpr() {
      this((TokenRange)null, "0");
   }

   @AllFieldsConstructor
   public DoubleLiteralExpr(final String value) {
      this((TokenRange)null, value);
   }

   public DoubleLiteralExpr(TokenRange tokenRange, String value) {
      super(tokenRange, value);
      this.customInitialization();
   }

   public DoubleLiteralExpr(final double value) {
      this((TokenRange)null, String.valueOf(value));
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

   public double asDouble() {
      return Double.parseDouble(this.value);
   }

   public DoubleLiteralExpr setDouble(double value) {
      this.value = String.valueOf(value);
      return this;
   }

   public DoubleLiteralExpr clone() {
      return (DoubleLiteralExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public DoubleLiteralExprMetaModel getMetaModel() {
      return JavaParserMetaModel.doubleLiteralExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isDoubleLiteralExpr() {
      return true;
   }

   public DoubleLiteralExpr asDoubleLiteralExpr() {
      return this;
   }

   public void ifDoubleLiteralExpr(Consumer<DoubleLiteralExpr> action) {
      action.accept(this);
   }

   public Optional<DoubleLiteralExpr> toDoubleLiteralExpr() {
      return Optional.of(this);
   }
}
