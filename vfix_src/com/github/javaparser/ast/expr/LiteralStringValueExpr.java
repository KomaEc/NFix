package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.LiteralStringValueExprMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class LiteralStringValueExpr extends LiteralExpr {
   protected String value;

   @AllFieldsConstructor
   public LiteralStringValueExpr(final String value) {
      this((TokenRange)null, value);
   }

   public LiteralStringValueExpr(TokenRange tokenRange, String value) {
      super(tokenRange);
      this.setValue(value);
      this.customInitialization();
   }

   public String getValue() {
      return this.value;
   }

   public LiteralStringValueExpr setValue(final String value) {
      Utils.assertNotNull(value);
      if (value == this.value) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.VALUE, this.value, value);
         this.value = value;
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public LiteralStringValueExpr clone() {
      return (LiteralStringValueExpr)this.accept(new CloneVisitor(), (Object)null);
   }

   public LiteralStringValueExprMetaModel getMetaModel() {
      return JavaParserMetaModel.literalStringValueExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isLiteralStringValueExpr() {
      return true;
   }

   public LiteralStringValueExpr asLiteralStringValueExpr() {
      return this;
   }

   public void ifLiteralStringValueExpr(Consumer<LiteralStringValueExpr> action) {
      action.accept(this);
   }

   public Optional<LiteralStringValueExpr> toLiteralStringValueExpr() {
      return Optional.of(this);
   }
}
