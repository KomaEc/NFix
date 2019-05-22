package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.BinaryExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.printer.Printable;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class BinaryExpr extends Expression {
   private Expression left;
   private Expression right;
   private BinaryExpr.Operator operator;

   public BinaryExpr() {
      this((TokenRange)null, new BooleanLiteralExpr(), new BooleanLiteralExpr(), BinaryExpr.Operator.EQUALS);
   }

   @AllFieldsConstructor
   public BinaryExpr(Expression left, Expression right, BinaryExpr.Operator operator) {
      this((TokenRange)null, left, right, operator);
   }

   public BinaryExpr(TokenRange tokenRange, Expression left, Expression right, BinaryExpr.Operator operator) {
      super(tokenRange);
      this.setLeft(left);
      this.setRight(right);
      this.setOperator(operator);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Expression getLeft() {
      return this.left;
   }

   public BinaryExpr.Operator getOperator() {
      return this.operator;
   }

   public Expression getRight() {
      return this.right;
   }

   public BinaryExpr setLeft(final Expression left) {
      Utils.assertNotNull(left);
      if (left == this.left) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.LEFT, this.left, left);
         if (this.left != null) {
            this.left.setParentNode((Node)null);
         }

         this.left = left;
         this.setAsParentNodeOf(left);
         return this;
      }
   }

   public BinaryExpr setOperator(final BinaryExpr.Operator operator) {
      Utils.assertNotNull(operator);
      if (operator == this.operator) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.OPERATOR, this.operator, operator);
         this.operator = operator;
         return this;
      }
   }

   public BinaryExpr setRight(final Expression right) {
      Utils.assertNotNull(right);
      if (right == this.right) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.RIGHT, this.right, right);
         if (this.right != null) {
            this.right.setParentNode((Node)null);
         }

         this.right = right;
         this.setAsParentNodeOf(right);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public BinaryExpr clone() {
      return (BinaryExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public BinaryExprMetaModel getMetaModel() {
      return JavaParserMetaModel.binaryExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.left) {
         this.setLeft((Expression)replacementNode);
         return true;
      } else if (node == this.right) {
         this.setRight((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isBinaryExpr() {
      return true;
   }

   public BinaryExpr asBinaryExpr() {
      return this;
   }

   public void ifBinaryExpr(Consumer<BinaryExpr> action) {
      action.accept(this);
   }

   public Optional<BinaryExpr> toBinaryExpr() {
      return Optional.of(this);
   }

   public static enum Operator implements Printable {
      OR("||"),
      AND("&&"),
      BINARY_OR("|"),
      BINARY_AND("&"),
      XOR("^"),
      EQUALS("=="),
      NOT_EQUALS("!="),
      LESS("<"),
      GREATER(">"),
      LESS_EQUALS("<="),
      GREATER_EQUALS(">="),
      LEFT_SHIFT("<<"),
      SIGNED_RIGHT_SHIFT(">>"),
      UNSIGNED_RIGHT_SHIFT(">>>"),
      PLUS("+"),
      MINUS("-"),
      MULTIPLY("*"),
      DIVIDE("/"),
      REMAINDER("%");

      private final String codeRepresentation;

      private Operator(String codeRepresentation) {
         this.codeRepresentation = codeRepresentation;
      }

      public String asString() {
         return this.codeRepresentation;
      }

      public Optional<AssignExpr.Operator> toAssignOperator() {
         switch(this) {
         case BINARY_OR:
            return Optional.of(AssignExpr.Operator.BINARY_OR);
         case BINARY_AND:
            return Optional.of(AssignExpr.Operator.BINARY_AND);
         case XOR:
            return Optional.of(AssignExpr.Operator.XOR);
         case LEFT_SHIFT:
            return Optional.of(AssignExpr.Operator.LEFT_SHIFT);
         case SIGNED_RIGHT_SHIFT:
            return Optional.of(AssignExpr.Operator.SIGNED_RIGHT_SHIFT);
         case UNSIGNED_RIGHT_SHIFT:
            return Optional.of(AssignExpr.Operator.UNSIGNED_RIGHT_SHIFT);
         case PLUS:
            return Optional.of(AssignExpr.Operator.PLUS);
         case MINUS:
            return Optional.of(AssignExpr.Operator.MINUS);
         case MULTIPLY:
            return Optional.of(AssignExpr.Operator.MULTIPLY);
         case DIVIDE:
            return Optional.of(AssignExpr.Operator.DIVIDE);
         case REMAINDER:
            return Optional.of(AssignExpr.Operator.REMAINDER);
         default:
            return Optional.empty();
         }
      }
   }
}
