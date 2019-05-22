package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.CharLiteralExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.StringEscapeUtils;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class CharLiteralExpr extends LiteralStringValueExpr {
   public CharLiteralExpr() {
      this((TokenRange)null, "?");
   }

   @AllFieldsConstructor
   public CharLiteralExpr(String value) {
      this((TokenRange)null, value);
   }

   public CharLiteralExpr(char value) {
      this((TokenRange)null, StringEscapeUtils.escapeJava(String.valueOf(value)));
   }

   public CharLiteralExpr(TokenRange tokenRange, String value) {
      super(tokenRange, value);
      this.customInitialization();
   }

   public static CharLiteralExpr escape(String string) {
      return new CharLiteralExpr(Utils.escapeEndOfLines(string));
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

   public char asChar() {
      return StringEscapeUtils.unescapeJava(this.value).charAt(0);
   }

   public CharLiteralExpr setChar(char value) {
      this.value = String.valueOf(value);
      return this;
   }

   public CharLiteralExpr clone() {
      return (CharLiteralExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public CharLiteralExprMetaModel getMetaModel() {
      return JavaParserMetaModel.charLiteralExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isCharLiteralExpr() {
      return true;
   }

   public CharLiteralExpr asCharLiteralExpr() {
      return this;
   }

   public void ifCharLiteralExpr(Consumer<CharLiteralExpr> action) {
      action.accept(this);
   }

   public Optional<CharLiteralExpr> toCharLiteralExpr() {
      return Optional.of(this);
   }
}
