package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.metamodel.SwitchEntryStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class SwitchEntryStmt extends Statement implements NodeWithStatements<SwitchEntryStmt> {
   @OptionalProperty
   private Expression label;
   private NodeList<Statement> statements;

   public SwitchEntryStmt() {
      this((TokenRange)null, (Expression)null, new NodeList());
   }

   @AllFieldsConstructor
   public SwitchEntryStmt(final Expression label, final NodeList<Statement> statements) {
      this((TokenRange)null, label, statements);
   }

   public SwitchEntryStmt(TokenRange tokenRange, Expression label, NodeList<Statement> statements) {
      super(tokenRange);
      this.setLabel(label);
      this.setStatements(statements);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<Expression> getLabel() {
      return Optional.ofNullable(this.label);
   }

   public NodeList<Statement> getStatements() {
      return this.statements;
   }

   public SwitchEntryStmt setLabel(final Expression label) {
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

   public SwitchEntryStmt setStatements(final NodeList<Statement> statements) {
      Utils.assertNotNull(statements);
      if (statements == this.statements) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.STATEMENTS, this.statements, statements);
         if (this.statements != null) {
            this.statements.setParentNode((Node)null);
         }

         this.statements = statements;
         this.setAsParentNodeOf(statements);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.label != null && node == this.label) {
         this.removeLabel();
         return true;
      } else {
         for(int i = 0; i < this.statements.size(); ++i) {
            if (this.statements.get(i) == node) {
               this.statements.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public SwitchEntryStmt removeLabel() {
      return this.setLabel((Expression)null);
   }

   public SwitchEntryStmt clone() {
      return (SwitchEntryStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public SwitchEntryStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.switchEntryStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.label != null && node == this.label) {
         this.setLabel((Expression)replacementNode);
         return true;
      } else {
         for(int i = 0; i < this.statements.size(); ++i) {
            if (this.statements.get(i) == node) {
               this.statements.set(i, (Node)((Statement)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isSwitchEntryStmt() {
      return true;
   }

   public SwitchEntryStmt asSwitchEntryStmt() {
      return this;
   }

   public void ifSwitchEntryStmt(Consumer<SwitchEntryStmt> action) {
      action.accept(this);
   }

   public Optional<SwitchEntryStmt> toSwitchEntryStmt() {
      return Optional.of(this);
   }
}
