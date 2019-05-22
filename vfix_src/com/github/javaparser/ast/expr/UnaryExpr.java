package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.DerivedProperty;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.UnaryExprMetaModel;
import com.github.javaparser.printer.Printable;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class UnaryExpr extends Expression implements NodeWithExpression<UnaryExpr> {
   private Expression expression;
   private UnaryExpr.Operator operator;

   public UnaryExpr() {
      this((TokenRange)null, new IntegerLiteralExpr(), UnaryExpr.Operator.POSTFIX_INCREMENT);
   }

   @AllFieldsConstructor
   public UnaryExpr(final Expression expression, final UnaryExpr.Operator operator) {
      this((TokenRange)null, expression, operator);
   }

   public UnaryExpr(TokenRange tokenRange, Expression expression, UnaryExpr.Operator operator) {
      super(tokenRange);
      this.setExpression(expression);
      this.setOperator(operator);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getExpression() {
      return this.expression;
   }

   public UnaryExpr.Operator getOperator() {
      return this.operator;
   }

   public UnaryExpr setExpression(final Expression expression) {
      Utils.assertNotNull(expression);
      if (expression == this.expression) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.EXPRESSION, this.expression, expression);
         if (this.expression != null) {
            this.expression.setParentNode((Node)null);
         }

         this.expression = expression;
         this.setAsParentNodeOf(expression);
         return this;
      }
   }

   public UnaryExpr setOperator(final UnaryExpr.Operator operator) {
      Utils.assertNotNull(operator);
      if (operator == this.operator) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.OPERATOR, this.operator, operator);
         this.operator = operator;
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   @DerivedProperty
   public boolean isPostfix() {
      return this.operator.isPostfix();
   }

   @DerivedProperty
   public boolean isPrefix() {
      return !this.isPostfix();
   }

   public UnaryExpr clone() {
      return (UnaryExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public UnaryExprMetaModel getMetaModel() {
      return JavaParserMetaModel.unaryExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.expression) {
         this.setExpression((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isUnaryExpr() {
      return true;
   }

   public UnaryExpr asUnaryExpr() {
      return this;
   }

   public void ifUnaryExpr(Consumer<UnaryExpr> action) {
      action.accept(this);
   }

   public Optional<UnaryExpr> toUnaryExpr() {
      return Optional.of(this);
   }

   public static enum Operator implements Printable {
      PLUS("+", false),
      MINUS("-", false),
      PREFIX_INCREMENT("++", false),
      PREFIX_DECREMENT("--", false),
      LOGICAL_COMPLEMENT("!", false),
      BITWISE_COMPLEMENT("~", false),
      POSTFIX_INCREMENT("++", true),
      POSTFIX_DECREMENT("--", true);

      private final String codeRepresentation;
      private final boolean isPostfix;

      private Operator(String codeRepresentation, boolean isPostfix) {
         this.codeRepresentation = codeRepresentation;
         this.isPostfix = isPostfix;
      }

      public String asString() {
         return this.codeRepresentation;
      }

      public boolean isPostfix() {
         return this.isPostfix;
      }

      public boolean isPrefix() {
         return !this.isPostfix();
      }
   }
}
