package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.SwitchStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class SwitchStmt extends Statement {
   private Expression selector;
   private NodeList<SwitchEntryStmt> entries;

   public SwitchStmt() {
      this((TokenRange)null, new NameExpr(), new NodeList());
   }

   @AllFieldsConstructor
   public SwitchStmt(final Expression selector, final NodeList<SwitchEntryStmt> entries) {
      this((TokenRange)null, selector, entries);
   }

   public SwitchStmt(TokenRange tokenRange, Expression selector, NodeList<SwitchEntryStmt> entries) {
      super(tokenRange);
      this.setSelector(selector);
      this.setEntries(entries);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<SwitchEntryStmt> getEntries() {
      return this.entries;
   }

   public SwitchEntryStmt getEntry(int i) {
      return (SwitchEntryStmt)this.getEntries().get(i);
   }

   public Expression getSelector() {
      return this.selector;
   }

   public SwitchStmt setEntries(final NodeList<SwitchEntryStmt> entries) {
      Utils.assertNotNull(entries);
      if (entries == this.entries) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ENTRIES, this.entries, entries);
         if (this.entries != null) {
            this.entries.setParentNode((Node)null);
         }

         this.entries = entries;
         this.setAsParentNodeOf(entries);
         return this;
      }
   }

   /** @deprecated */
   @Deprecated
   public SwitchStmt setEntry(int i, SwitchEntryStmt entry) {
      this.getEntries().set(i, (Node)entry);
      return this;
   }

   /** @deprecated */
   @Deprecated
   public SwitchStmt addEntry(SwitchEntryStmt entry) {
      this.getEntries().add((Node)entry);
      return this;
   }

   public SwitchStmt setSelector(final Expression selector) {
      Utils.assertNotNull(selector);
      if (selector == this.selector) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.SELECTOR, this.selector, selector);
         if (this.selector != null) {
            this.selector.setParentNode((Node)null);
         }

         this.selector = selector;
         this.setAsParentNodeOf(selector);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.entries.size(); ++i) {
            if (this.entries.get(i) == node) {
               this.entries.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public SwitchStmt clone() {
      return (SwitchStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public SwitchStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.switchStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.entries.size(); ++i) {
            if (this.entries.get(i) == node) {
               this.entries.set(i, (Node)((SwitchEntryStmt)replacementNode));
               return true;
            }
         }

         if (node == this.selector) {
            this.setSelector((Expression)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }

   public boolean isSwitchStmt() {
      return true;
   }

   public SwitchStmt asSwitchStmt() {
      return this;
   }

   public void ifSwitchStmt(Consumer<SwitchStmt> action) {
      action.accept(this);
   }

   public Optional<SwitchStmt> toSwitchStmt() {
      return Optional.of(this);
   }
}
