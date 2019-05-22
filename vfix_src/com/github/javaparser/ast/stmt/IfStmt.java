package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithCondition;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.DerivedProperty;
import com.github.javaparser.metamodel.IfStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class IfStmt extends Statement implements NodeWithCondition<IfStmt> {
   private Expression condition;
   private Statement thenStmt;
   @OptionalProperty
   private Statement elseStmt;

   public IfStmt() {
      this((TokenRange)null, new BooleanLiteralExpr(), new ReturnStmt(), (Statement)null);
   }

   @AllFieldsConstructor
   public IfStmt(final Expression condition, final Statement thenStmt, final Statement elseStmt) {
      this((TokenRange)null, condition, thenStmt, elseStmt);
   }

   public IfStmt(TokenRange tokenRange, Expression condition, Statement thenStmt, Statement elseStmt) {
      super(tokenRange);
      this.setCondition(condition);
      this.setThenStmt(thenStmt);
      this.setElseStmt(elseStmt);
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

   public Optional<Statement> getElseStmt() {
      return Optional.ofNullable(this.elseStmt);
   }

   public Statement getThenStmt() {
      return this.thenStmt;
   }

   public IfStmt setCondition(final Expression condition) {
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

   public IfStmt setElseStmt(final Statement elseStmt) {
      if (elseStmt == this.elseStmt) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ELSE_STMT, this.elseStmt, elseStmt);
         if (this.elseStmt != null) {
            this.elseStmt.setParentNode((Node)null);
         }

         this.elseStmt = elseStmt;
         this.setAsParentNodeOf(elseStmt);
         return this;
      }
   }

   public IfStmt setThenStmt(final Statement thenStmt) {
      Utils.assertNotNull(thenStmt);
      if (thenStmt == this.thenStmt) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.THEN_STMT, this.thenStmt, thenStmt);
         if (this.thenStmt != null) {
            this.thenStmt.setParentNode((Node)null);
         }

         this.thenStmt = thenStmt;
         this.setAsParentNodeOf(thenStmt);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.elseStmt != null && node == this.elseStmt) {
         this.removeElseStmt();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public IfStmt removeElseStmt() {
      return this.setElseStmt((Statement)null);
   }

   @DerivedProperty
   public boolean hasThenBlock() {
      return this.thenStmt instanceof BlockStmt;
   }

   @DerivedProperty
   public boolean hasElseBlock() {
      return this.elseStmt instanceof BlockStmt;
   }

   @DerivedProperty
   public boolean hasCascadingIfStmt() {
      return this.elseStmt instanceof IfStmt;
   }

   @DerivedProperty
   public boolean hasElseBranch() {
      return this.elseStmt != null;
   }

   public IfStmt clone() {
      return (IfStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public IfStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.ifStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.condition) {
         this.setCondition((Expression)replacementNode);
         return true;
      } else if (this.elseStmt != null && node == this.elseStmt) {
         this.setElseStmt((Statement)replacementNode);
         return true;
      } else if (node == this.thenStmt) {
         this.setThenStmt((Statement)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isIfStmt() {
      return true;
   }

   public IfStmt asIfStmt() {
      return this;
   }

   public void ifIfStmt(Consumer<IfStmt> action) {
      action.accept(this);
   }

   public Optional<IfStmt> toIfStmt() {
      return Optional.of(this);
   }
}
