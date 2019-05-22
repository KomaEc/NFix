package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.BooleanLiteralExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class BooleanLiteralExpr extends LiteralExpr {
   private boolean value;

   public BooleanLiteralExpr() {
      this((TokenRange)null, false);
   }

   @AllFieldsConstructor
   public BooleanLiteralExpr(boolean value) {
      this((TokenRange)null, value);
   }

   public BooleanLiteralExpr(TokenRange tokenRange, boolean value) {
      super(tokenRange);
      this.setValue(value);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean getValue() {
      return this.value;
   }

   public BooleanLiteralExpr setValue(final boolean value) {
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

   public BooleanLiteralExpr clone() {
      return (BooleanLiteralExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public BooleanLiteralExprMetaModel getMetaModel() {
      return JavaParserMetaModel.booleanLiteralExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isBooleanLiteralExpr() {
      return true;
   }

   public BooleanLiteralExpr asBooleanLiteralExpr() {
      return this;
   }

   public void ifBooleanLiteralExpr(Consumer<BooleanLiteralExpr> action) {
      action.accept(this);
   }

   public Optional<BooleanLiteralExpr> toBooleanLiteralExpr() {
      return Optional.of(this);
   }
}
