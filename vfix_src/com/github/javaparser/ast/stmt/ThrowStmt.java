package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ThrowStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ThrowStmt extends Statement implements NodeWithExpression<ThrowStmt> {
   private Expression expression;

   public ThrowStmt() {
      this((TokenRange)null, new NameExpr());
   }

   @AllFieldsConstructor
   public ThrowStmt(final Expression expression) {
      this((TokenRange)null, expression);
   }

   public ThrowStmt(TokenRange tokenRange, Expression expression) {
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

   public ThrowStmt setExpression(final Expression expression) {
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

   public ThrowStmt clone() {
      return (ThrowStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ThrowStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.throwStmtMetaModel;
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

   public boolean isThrowStmt() {
      return true;
   }

   public ThrowStmt asThrowStmt() {
      return this;
   }

   public void ifThrowStmt(Consumer<ThrowStmt> action) {
      action.accept(this);
   }

   public Optional<ThrowStmt> toThrowStmt() {
      return Optional.of(this);
   }
}
