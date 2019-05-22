package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.BreakStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import java.util.Optional;
import java.util.function.Consumer;

public final class BreakStmt extends Statement {
   @OptionalProperty
   private SimpleName label;

   public BreakStmt() {
      this((TokenRange)null, new SimpleName());
   }

   public BreakStmt(final String label) {
      this((TokenRange)null, new SimpleName(label));
   }

   @AllFieldsConstructor
   public BreakStmt(final SimpleName label) {
      this((TokenRange)null, label);
   }

   public BreakStmt(TokenRange tokenRange, SimpleName label) {
      super(tokenRange);
      this.setLabel(label);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<SimpleName> getLabel() {
      return Optional.ofNullable(this.label);
   }

   public BreakStmt setLabel(final SimpleName label) {
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
      if (node == null) {
         return false;
      } else if (this.label != null && node == this.label) {
         this.removeLabel();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public BreakStmt removeLabel() {
      return this.setLabel((SimpleName)null);
   }

   public BreakStmt clone() {
      return (BreakStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public BreakStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.breakStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.label != null && node == this.label) {
         this.setLabel((SimpleName)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isBreakStmt() {
      return true;
   }

   public BreakStmt asBreakStmt() {
      return this;
   }

   public void ifBreakStmt(Consumer<BreakStmt> action) {
      action.accept(this);
   }

   public Optional<BreakStmt> toBreakStmt() {
      return Optional.of(this);
   }
}
