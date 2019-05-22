package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ForeachStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ForeachStmt extends Statement implements NodeWithBody<ForeachStmt> {
   private VariableDeclarationExpr variable;
   private Expression iterable;
   private Statement body;

   public ForeachStmt() {
      this((TokenRange)null, new VariableDeclarationExpr(), new NameExpr(), new ReturnStmt());
   }

   @AllFieldsConstructor
   public ForeachStmt(final VariableDeclarationExpr variable, final Expression iterable, final Statement body) {
      this((TokenRange)null, variable, iterable, body);
   }

   public ForeachStmt(TokenRange tokenRange, VariableDeclarationExpr variable, Expression iterable, Statement body) {
      super(tokenRange);
      this.setVariable(variable);
      this.setIterable(iterable);
      this.setBody(body);
      this.customInitialization();
   }

   public ForeachStmt(VariableDeclarationExpr variable, String iterable, BlockStmt body) {
      this((TokenRange)null, variable, new NameExpr(iterable), body);
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

   public Expression getIterable() {
      return this.iterable;
   }

   public VariableDeclarationExpr getVariable() {
      return this.variable;
   }

   public ForeachStmt setBody(final Statement body) {
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

   public ForeachStmt setIterable(final Expression iterable) {
      Utils.assertNotNull(iterable);
      if (iterable == this.iterable) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ITERABLE, this.iterable, iterable);
         if (this.iterable != null) {
            this.iterable.setParentNode((Node)null);
         }

         this.iterable = iterable;
         this.setAsParentNodeOf(iterable);
         return this;
      }
   }

   public ForeachStmt setVariable(final VariableDeclarationExpr variable) {
      Utils.assertNotNull(variable);
      if (variable == this.variable) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.VARIABLE, this.variable, variable);
         if (this.variable != null) {
            this.variable.setParentNode((Node)null);
         }

         this.variable = variable;
         this.setAsParentNodeOf(variable);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ForeachStmt clone() {
      return (ForeachStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ForeachStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.foreachStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.body) {
         this.setBody((Statement)replacementNode);
         return true;
      } else if (node == this.iterable) {
         this.setIterable((Expression)replacementNode);
         return true;
      } else if (node == this.variable) {
         this.setVariable((VariableDeclarationExpr)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isForeachStmt() {
      return true;
   }

   public ForeachStmt asForeachStmt() {
      return this;
   }

   public void ifForeachStmt(Consumer<ForeachStmt> action) {
      action.accept(this);
   }

   public Optional<ForeachStmt> toForeachStmt() {
      return Optional.of(this);
   }
}
