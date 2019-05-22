package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.nodeTypes.NodeWithCondition;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.WhileStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class WhileStmt extends Statement implements NodeWithBody<WhileStmt>, NodeWithCondition<WhileStmt> {
   private Expression condition;
   private Statement body;

   public WhileStmt() {
      this((TokenRange)null, new BooleanLiteralExpr(), new ReturnStmt());
   }

   @AllFieldsConstructor
   public WhileStmt(final Expression condition, final Statement body) {
      this((TokenRange)null, condition, body);
   }

   public WhileStmt(TokenRange tokenRange, Expression condition, Statement body) {
      super(tokenRange);
      this.setCondition(condition);
      this.setBody(body);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Statement getBody() {
      return this.body;
   }

   public Expression getCondition() {
      return this.condition;
   }

   public WhileStmt setBody(final Statement body) {
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

   public WhileStmt setCondition(final Expression condition) {
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

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public WhileStmt clone() {
      return (WhileStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public WhileStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.whileStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.body) {
         this.setBody((Statement)replacementNode);
         return true;
      } else if (node == this.condition) {
         this.setCondition((Expression)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isWhileStmt() {
      return true;
   }

   public WhileStmt asWhileStmt() {
      return this;
   }

   public void ifWhileStmt(Consumer<WhileStmt> action) {
      action.accept(this);
   }

   public Optional<WhileStmt> toWhileStmt() {
      return Optional.of(this);
   }
}
