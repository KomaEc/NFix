package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ArrayInitializerExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ArrayInitializerExpr extends Expression {
   private NodeList<Expression> values;

   public ArrayInitializerExpr() {
      this((TokenRange)null, new NodeList());
   }

   @AllFieldsConstructor
   public ArrayInitializerExpr(NodeList<Expression> values) {
      this((TokenRange)null, values);
   }

   public ArrayInitializerExpr(TokenRange tokenRange, NodeList<Expression> values) {
      super(tokenRange);
      this.setValues(values);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<Expression> getValues() {
      return this.values;
   }

   public ArrayInitializerExpr setValues(final NodeList<Expression> values) {
      Utils.assertNotNull(values);
      if (values == this.values) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.VALUES, this.values, values);
         if (this.values != null) {
            this.values.setParentNode((Node)null);
         }

         this.values = values;
         this.setAsParentNodeOf(values);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.values.size(); ++i) {
            if (this.values.get(i) == node) {
               this.values.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public ArrayInitializerExpr clone() {
      return (ArrayInitializerExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ArrayInitializerExprMetaModel getMetaModel() {
      return JavaParserMetaModel.arrayInitializerExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.values.size(); ++i) {
            if (this.values.get(i) == node) {
               this.values.set(i, (Node)((Expression)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isArrayInitializerExpr() {
      return true;
   }

   public ArrayInitializerExpr asArrayInitializerExpr() {
      return this;
   }

   public void ifArrayInitializerExpr(Consumer<ArrayInitializerExpr> action) {
      action.accept(this);
   }

   public Optional<ArrayInitializerExpr> toArrayInitializerExpr() {
      return Optional.of(this);
   }
}
