package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.IntegerLiteralExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class IntegerLiteralExpr extends LiteralStringValueExpr {
   public IntegerLiteralExpr() {
      this((TokenRange)null, "0");
   }

   @AllFieldsConstructor
   public IntegerLiteralExpr(final String value) {
      this((TokenRange)null, value);
   }

   public IntegerLiteralExpr(TokenRange tokenRange, String value) {
      super(tokenRange, value);
      this.customInitialization();
   }

   public IntegerLiteralExpr(final int value) {
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

   public int asInt() {
      String result = this.value.replaceAll("_", "");
      if (!result.startsWith("0x") && !result.startsWith("0X")) {
         if (!result.startsWith("0b") && !result.startsWith("0B")) {
            return result.length() > 1 && result.startsWith("0") ? Integer.parseUnsignedInt(result.substring(1), 8) : Integer.parseInt(result);
         } else {
            return Integer.parseUnsignedInt(result.substring(2), 2);
         }
      } else {
         return Integer.parseUnsignedInt(result.substring(2), 16);
      }
   }

   public IntegerLiteralExpr setInt(int value) {
      this.value = String.valueOf(value);
      return this;
   }

   public IntegerLiteralExpr clone() {
      return (IntegerLiteralExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public IntegerLiteralExprMetaModel getMetaModel() {
      return JavaParserMetaModel.integerLiteralExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isIntegerLiteralExpr() {
      return true;
   }

   public IntegerLiteralExpr asIntegerLiteralExpr() {
      return this;
   }

   public void ifIntegerLiteralExpr(Consumer<IntegerLiteralExpr> action) {
      action.accept(this);
   }

   public Optional<IntegerLiteralExpr> toIntegerLiteralExpr() {
      return Optional.of(this);
   }
}
