package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.SynchronizedStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class SynchronizedStmt extends Statement implements NodeWithBlockStmt<SynchronizedStmt>, NodeWithExpression<SynchronizedStmt> {
   private Expression expression;
   private BlockStmt body;

   public SynchronizedStmt() {
      this((TokenRange)null, new NameExpr(), new BlockStmt());
   }

   @AllFieldsConstructor
   public SynchronizedStmt(final Expression expression, final BlockStmt body) {
      this((TokenRange)null, expression, body);
   }

   public SynchronizedStmt(TokenRange tokenRange, Expression expression, BlockStmt body) {
      super(tokenRange);
      this.setExpression(expression);
      this.setBody(body);
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

   public SynchronizedStmt setExpression(final Expression expression) {
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

   public BlockStmt getBody() {
      return this.body;
   }

   public SynchronizedStmt setBody(final BlockStmt body) {
      Utils.assertNotNull(body);
      if (body == this.body) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.BODY, this.body, body);
         if (this.body != null) {
            this.body.setParentNode((Node)null);
         }

         this.body = body;
         this.setAsParentNodeOf(body);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public SynchronizedStmt clone() {
      return (SynchronizedStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public SynchronizedStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.synchronizedStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.body) {
         this.setBody((BlockStmt)replacementNode);
         return true;
      } else if (node == this.expression) {
         this.setExpression((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isSynchronizedStmt() {
      return true;
   }

   public SynchronizedStmt asSynchronizedStmt() {
      return this;
   }

   public void ifSynchronizedStmt(Consumer<SynchronizedStmt> action) {
      action.accept(this);
   }

   public Optional<SynchronizedStmt> toSynchronizedStmt() {
      return Optional.of(this);
   }
}
