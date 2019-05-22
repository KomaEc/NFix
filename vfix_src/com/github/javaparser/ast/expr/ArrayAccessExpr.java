package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ArrayAccessExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ArrayAccessExpr extends Expression {
   private Expression name;
   private Expression index;

   public ArrayAccessExpr() {
      this((TokenRange)null, new NameExpr(), new IntegerLiteralExpr());
   }

   @AllFieldsConstructor
   public ArrayAccessExpr(Expression name, Expression index) {
      this((TokenRange)null, name, index);
   }

   public ArrayAccessExpr(TokenRange tokenRange, Expression name, Expression index) {
      super(tokenRange);
      this.setName(name);
      this.setIndex(index);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getIndex() {
      return this.index;
   }

   public Expression getName() {
      return this.name;
   }

   public ArrayAccessExpr setIndex(final Expression index) {
      Utils.assertNotNull(index);
      if (index == this.index) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.INDEX, this.index, index);
         if (this.index != null) {
            this.index.setParentNode((Node)null);
         }

         this.index = index;
         this.setAsParentNodeOf(index);
         return this;
      }
   }

   public ArrayAccessExpr setName(final Expression name) {
      Utils.assertNotNull(name);
      if (name == this.name) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.NAME, this.name, name);
         if (this.name != null) {
            this.name.setParentNode((Node)null);
         }

         this.name = name;
         this.setAsParentNodeOf(name);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ArrayAccessExpr clone() {
      return (ArrayAccessExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ArrayAccessExprMetaModel getMetaModel() {
      return JavaParserMetaModel.arrayAccessExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.index) {
         this.setIndex((Expression)replacementNode);
         return true;
      } else if (node == this.name) {
         this.setName((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isArrayAccessExpr() {
      return true;
   }

   public ArrayAccessExpr asArrayAccessExpr() {
      return this;
   }

   public void ifArrayAccessExpr(Consumer<ArrayAccessExpr> action) {
      action.accept(this);
   }

   public Optional<ArrayAccessExpr> toArrayAccessExpr() {
      return Optional.of(this);
   }
}
