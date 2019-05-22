package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.metamodel.ReturnStmtMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class ReturnStmt extends Statement {
   @OptionalProperty
   private Expression expression;

   public ReturnStmt() {
      this((TokenRange)null, (Expression)null);
   }

   @AllFieldsConstructor
   public ReturnStmt(final Expression expression) {
      this((TokenRange)null, expression);
   }

   public ReturnStmt(TokenRange tokenRange, Expression expression) {
      super(tokenRange);
      this.setExpression(expression);
      this.customInitialization();
   }

   public ReturnStmt(String expression) {
      this((TokenRange)null, new NameExpr(expression));
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<Expression> getExpression() {
      return Optional.ofNullable(this.expression);
   }

   public ReturnStmt setExpression(final Expression expression) {
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

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.expression != null && node == this.expression) {
         this.removeExpression();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public ReturnStmt removeExpression() {
      return this.setExpression((Expression)null);
   }

   public ReturnStmt clone() {
      return (ReturnStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ReturnStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.returnStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.expression != null && node == this.expression) {
         this.setExpression((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isReturnStmt() {
      return true;
   }

   public ReturnStmt asReturnStmt() {
      return this;
   }

   public void ifReturnStmt(Consumer<ReturnStmt> action) {
      action.accept(this);
   }

   public Optional<ReturnStmt> toReturnStmt() {
      return Optional.of(this);
   }
}
