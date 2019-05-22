package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.StringLiteralExprMetaModel;
import com.github.javaparser.utils.StringEscapeUtils;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class StringLiteralExpr extends LiteralStringValueExpr {
   public StringLiteralExpr() {
      this((TokenRange)null, "empty");
   }

   @AllFieldsConstructor
   public StringLiteralExpr(final String value) {
      this((TokenRange)null, Utils.escapeEndOfLines(value));
   }

   /** @deprecated */
   @Deprecated
   public static StringLiteralExpr escape(String string) {
      return new StringLiteralExpr(Utils.escapeEndOfLines(string));
   }

   public StringLiteralExpr(TokenRange tokenRange, String value) {
      super(tokenRange, value);
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

   public StringLiteralExpr setEscapedValue(String value) {
      this.value = Utils.escapeEndOfLines(value);
      return this;
   }

   public String asString() {
      return StringEscapeUtils.unescapeJava(this.value);
   }

   public StringLiteralExpr setString(String value) {
      this.value = StringEscapeUtils.escapeJava(value);
      return this;
   }

   public StringLiteralExpr clone() {
      return (StringLiteralExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public StringLiteralExprMetaModel getMetaModel() {
      return JavaParserMetaModel.stringLiteralExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isStringLiteralExpr() {
      return true;
   }

   public StringLiteralExpr asStringLiteralExpr() {
      return this;
   }

   public void ifStringLiteralExpr(Consumer<StringLiteralExpr> action) {
      action.accept(this);
   }

   public Optional<StringLiteralExpr> toStringLiteralExpr() {
      return Optional.of(this);
   }
}
