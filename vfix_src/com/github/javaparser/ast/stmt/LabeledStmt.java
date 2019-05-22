package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.LabeledStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class LabeledStmt extends Statement {
   private SimpleName label;
   private Statement statement;

   public LabeledStmt() {
      this((TokenRange)null, new SimpleName(), new ReturnStmt());
   }

   public LabeledStmt(final String label, final Statement statement) {
      this((TokenRange)null, new SimpleName(label), statement);
   }

   @AllFieldsConstructor
   public LabeledStmt(final SimpleName label, final Statement statement) {
      this((TokenRange)null, label, statement);
   }

   public LabeledStmt(TokenRange tokenRange, SimpleName label, Statement statement) {
      super(tokenRange);
      this.setLabel(label);
      this.setStatement(statement);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Statement getStatement() {
      return this.statement;
   }

   public LabeledStmt setStatement(final Statement statement) {
      Utils.assertNotNull(statement);
      if (statement == this.statement) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.STATEMENT, this.statement, statement);
         if (this.statement != null) {
            this.statement.setParentNode((Node)null);
         }

         this.statement = statement;
         this.setAsParentNodeOf(statement);
         return this;
      }
   }

   public SimpleName getLabel() {
      return this.label;
   }

   public LabeledStmt setLabel(final SimpleName label) {
      Utils.assertNotNull(label);
      if (label == this.label) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.LABEL, this.label, label);
         if (this.label != null) {
            this.label.setParentNode((Node)null);
         }

         this.label = label;
         this.setAsParentNodeOf(label);
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public LabeledStmt clone() {
      return (LabeledStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public LabeledStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.labeledStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.label) {
         this.setLabel((SimpleName)replacementNode);
         return true;
      } else if (node == this.statement) {
         this.setStatement((Statement)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isLabeledStmt() {
      return true;
   }

   public LabeledStmt asLabeledStmt() {
      return this;
   }

   public void ifLabeledStmt(Consumer<LabeledStmt> action) {
      action.accept(this);
   }

   public Optional<LabeledStmt> toLabeledStmt() {
      return Optional.of(this);
   }
}
