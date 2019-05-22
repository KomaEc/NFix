package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ExpressionStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ExpressionStmt extends Statement implements NodeWithExpression<ExpressionStmt> {
   private Expression expression;

   public ExpressionStmt() {
      this((TokenRange)null, new BooleanLiteralExpr());
   }

   @AllFieldsConstructor
   public ExpressionStmt(final Expression expression) {
      this((TokenRange)null, expression);
   }

   public ExpressionStmt(TokenRange tokenRange, Expression expression) {
      super(tokenRange);
      this.setExpression(expression);
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

   public ExpressionStmt setExpression(final Expression expression) {
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

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ExpressionStmt clone() {
      return (ExpressionStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ExpressionStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.expressionStmtMetaModel;
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

   public boolean isExpressionStmt() {
      return true;
   }

   public ExpressionStmt asExpressionStmt() {
      return this;
   }

   public void ifExpressionStmt(Consumer<ExpressionStmt> action) {
      action.accept(this);
   }

   public Optional<ExpressionStmt> toExpressionStmt() {
      return Optional.of(this);
   }
}
