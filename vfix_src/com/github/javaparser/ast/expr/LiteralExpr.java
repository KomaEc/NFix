package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.LiteralExprMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class LiteralExpr extends Expression {
   @AllFieldsConstructor
   public LiteralExpr() {
      this((TokenRange)null);
   }

   public LiteralExpr(TokenRange tokenRange) {
      super(tokenRange);
      this.customInitialization();
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public LiteralExpr clone() {
      return (LiteralExpr)this.accept(new CloneVisitor(), (Object)null);
   }

   public LiteralExprMetaModel getMetaModel() {
      return JavaParserMetaModel.literalExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isLiteralExpr() {
      return true;
   }

   public LiteralExpr asLiteralExpr() {
      return this;
   }

   public void ifLiteralExpr(Consumer<LiteralExpr> action) {
      action.accept(this);
   }

   public Optional<LiteralExpr> toLiteralExpr() {
      return Optional.of(this);
   }
}
