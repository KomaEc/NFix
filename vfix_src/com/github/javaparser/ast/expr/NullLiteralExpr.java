package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NullLiteralExprMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class NullLiteralExpr extends LiteralExpr {
   @AllFieldsConstructor
   public NullLiteralExpr() {
      this((TokenRange)null);
   }

   public NullLiteralExpr(TokenRange tokenRange) {
      super(tokenRange);
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

   public NullLiteralExpr clone() {
      return (NullLiteralExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public NullLiteralExprMetaModel getMetaModel() {
      return JavaParserMetaModel.nullLiteralExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isNullLiteralExpr() {
      return true;
   }

   public NullLiteralExpr asNullLiteralExpr() {
      return this;
   }

   public void ifNullLiteralExpr(Consumer<NullLiteralExpr> action) {
      action.accept(this);
   }

   public Optional<NullLiteralExpr> toNullLiteralExpr() {
      return Optional.of(this);
   }
}
