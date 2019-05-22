package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.LongLiteralExprMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class LongLiteralExpr extends LiteralStringValueExpr {
   public LongLiteralExpr() {
      this((TokenRange)null, "0");
   }

   @AllFieldsConstructor
   public LongLiteralExpr(final String value) {
      this((TokenRange)null, value);
   }

   public LongLiteralExpr(TokenRange tokenRange, String value) {
      super(tokenRange, value);
      this.customInitialization();
   }

   public LongLiteralExpr(final long value) {
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

   public long asLong() {
      String result = this.value.replaceAll("_", "");
      char lastChar = result.charAt(result.length() - 1);
      if (lastChar == 'l' || lastChar == 'L') {
         result = result.substring(0, result.length() - 1);
      }

      if (!result.startsWith("0x") && !result.startsWith("0X")) {
         if (!result.startsWith("0b") && !result.startsWith("0B")) {
            return result.length() > 1 && result.startsWith("0") ? Long.parseUnsignedLong(result.substring(1), 8) : Long.parseLong(result);
         } else {
            return Long.parseUnsignedLong(result.substring(2), 2);
         }
      } else {
         return Long.parseUnsignedLong(result.substring(2), 16);
      }
   }

   public LongLiteralExpr setLong(long value) {
      this.value = String.valueOf(value);
      return this;
   }

   public LongLiteralExpr clone() {
      return (LongLiteralExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public LongLiteralExprMetaModel getMetaModel() {
      return JavaParserMetaModel.longLiteralExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isLongLiteralExpr() {
      return true;
   }

   public LongLiteralExpr asLongLiteralExpr() {
      return this;
   }

   public void ifLongLiteralExpr(Consumer<LongLiteralExpr> action) {
      action.accept(this);
   }

   public Optional<LongLiteralExpr> toLongLiteralExpr() {
      return Optional.of(this);
   }
}
