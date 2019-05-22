package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.metamodel.SuperExprMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class SuperExpr extends Expression {
   @OptionalProperty
   private Expression classExpr;

   public SuperExpr() {
      this((TokenRange)null, (Expression)null);
   }

   @AllFieldsConstructor
   public SuperExpr(final Expression classExpr) {
      this((TokenRange)null, classExpr);
   }

   public SuperExpr(TokenRange tokenRange, Expression classExpr) {
      super(tokenRange);
      this.setClassExpr(classExpr);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<Expression> getClassExpr() {
      return Optional.ofNullable(this.classExpr);
   }

   public SuperExpr setClassExpr(final Expression classExpr) {
      if (classExpr == this.classExpr) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.CLASS_EXPR, this.classExpr, classExpr);
         if (this.classExpr != null) {
            this.classExpr.setParentNode((Node)null);
         }

         this.classExpr = classExpr;
         this.setAsParentNodeOf(classExpr);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.classExpr != null && node == this.classExpr) {
         this.removeClassExpr();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public SuperExpr removeClassExpr() {
      return this.setClassExpr((Expression)null);
   }

   public SuperExpr clone() {
      return (SuperExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public SuperExprMetaModel getMetaModel() {
      return JavaParserMetaModel.superExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.classExpr != null && node == this.classExpr) {
         this.setClassExpr((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isSuperExpr() {
      return true;
   }

   public SuperExpr asSuperExpr() {
      return this;
   }

   public void ifSuperExpr(Consumer<SuperExpr> action) {
      action.accept(this);
   }

   public Optional<SuperExpr> toSuperExpr() {
      return Optional.of(this);
   }
}
