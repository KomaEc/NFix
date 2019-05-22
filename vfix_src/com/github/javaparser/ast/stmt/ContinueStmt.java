package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalLabel;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ContinueStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import java.util.Optional;
import java.util.function.Consumer;

public final class ContinueStmt extends Statement implements NodeWithOptionalLabel<ContinueStmt> {
   @OptionalProperty
   private SimpleName label;

   public ContinueStmt() {
      this((TokenRange)null, (SimpleName)null);
   }

   public ContinueStmt(final String label) {
      this((TokenRange)null, new SimpleName(label));
   }

   @AllFieldsConstructor
   public ContinueStmt(final SimpleName label) {
      this((TokenRange)null, label);
   }

   public ContinueStmt(TokenRange tokenRange, SimpleName label) {
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

   public ContinueStmt setLabel(final SimpleName label) {
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

   public ContinueStmt removeLabel() {
      return this.setLabel((SimpleName)null);
   }

   public ContinueStmt clone() {
      return (ContinueStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ContinueStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.continueStmtMetaModel;
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

   public boolean isContinueStmt() {
      return true;
   }

   public ContinueStmt asContinueStmt() {
      return this;
   }

   public void ifContinueStmt(Consumer<ContinueStmt> action) {
      action.accept(this);
   }

   public Optional<ContinueStmt> toContinueStmt() {
      return Optional.of(this);
   }
}
