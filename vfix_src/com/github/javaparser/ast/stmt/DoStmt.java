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
import com.github.javaparser.metamodel.DoStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class DoStmt extends Statement implements NodeWithBody<DoStmt>, NodeWithCondition<DoStmt> {
   private Statement body;
   private Expression condition;

   public DoStmt() {
      this((TokenRange)null, new ReturnStmt(), new BooleanLiteralExpr());
   }

   @AllFieldsConstructor
   public DoStmt(final Statement body, final Expression condition) {
      this((TokenRange)null, body, condition);
   }

   public DoStmt(TokenRange tokenRange, Statement body, Expression condition) {
      super(tokenRange);
      this.setBody(body);
      this.setCondition(condition);
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

   public DoStmt setBody(final Statement body) {
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

   public DoStmt setCondition(final Expression condition) {
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

   public DoStmt clone() {
      return (DoStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public DoStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.doStmtMetaModel;
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

   public boolean isDoStmt() {
      return true;
   }

   public DoStmt asDoStmt() {
      return this;
   }

   public void ifDoStmt(Consumer<DoStmt> action) {
      action.accept(this);
   }

   public Optional<DoStmt> toDoStmt() {
      return Optional.of(this);
   }
}
