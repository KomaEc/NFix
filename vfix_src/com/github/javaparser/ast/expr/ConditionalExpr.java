package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithCondition;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ConditionalExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ConditionalExpr extends Expression implements NodeWithCondition<ConditionalExpr> {
   private Expression condition;
   private Expression thenExpr;
   private Expression elseExpr;

   public ConditionalExpr() {
      this((TokenRange)null, new BooleanLiteralExpr(), new StringLiteralExpr(), new StringLiteralExpr());
   }

   @AllFieldsConstructor
   public ConditionalExpr(Expression condition, Expression thenExpr, Expression elseExpr) {
      this((TokenRange)null, condition, thenExpr, elseExpr);
   }

   public ConditionalExpr(TokenRange tokenRange, Expression condition, Expression thenExpr, Expression elseExpr) {
      super(tokenRange);
      this.setCondition(condition);
      this.setThenExpr(thenExpr);
      this.setElseExpr(elseExpr);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getCondition() {
      return this.condition;
   }

   public Expression getElseExpr() {
      return this.elseExpr;
   }

   public Expression getThenExpr() {
      return this.thenExpr;
   }

   public ConditionalExpr setCondition(final Expression condition) {
      Utils.assertNotNull(condition);
      if (condition == this.condition) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.CONDITION, this.condition, condition);
         if (this.condition != null) {
            this.condition.setParentNode((Node)null);
         }

         this.condition = condition;
         this.setAsParentNodeOf(condition);
         return this;
      }
   }

   public ConditionalExpr setElseExpr(final Expression elseExpr) {
      Utils.assertNotNull(elseExpr);
      if (elseExpr == this.elseExpr) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ELSE_EXPR, this.elseExpr, elseExpr);
         if (this.elseExpr != null) {
            this.elseExpr.setParentNode((Node)null);
         }

         this.elseExpr = elseExpr;
         this.setAsParentNodeOf(elseExpr);
         return this;
      }
   }

   public ConditionalExpr setThenExpr(final Expression thenExpr) {
      Utils.assertNotNull(thenExpr);
      if (thenExpr == this.thenExpr) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.THEN_EXPR, this.thenExpr, thenExpr);
         if (this.thenExpr != null) {
            this.thenExpr.setParentNode((Node)null);
         }

         this.thenExpr = thenExpr;
         this.setAsParentNodeOf(thenExpr);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ConditionalExpr clone() {
      return (ConditionalExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ConditionalExprMetaModel getMetaModel() {
      return JavaParserMetaModel.conditionalExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.condition) {
         this.setCondition((Expression)replacementNode);
         return true;
      } else if (node == this.elseExpr) {
         this.setElseExpr((Expression)replacementNode);
         return true;
      } else if (node == this.thenExpr) {
         this.setThenExpr((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isConditionalExpr() {
      return true;
   }

   public ConditionalExpr asConditionalExpr() {
      return this;
   }

   public void ifConditionalExpr(Consumer<ConditionalExpr> action) {
      action.accept(this);
   }

   public Optional<ConditionalExpr> toConditionalExpr() {
      return Optional.of(this);
   }
}
