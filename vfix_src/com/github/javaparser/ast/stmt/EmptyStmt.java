package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.EmptyStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class EmptyStmt extends Statement {
   @AllFieldsConstructor
   public EmptyStmt() {
      this((TokenRange)null);
   }

   public EmptyStmt(TokenRange tokenRange) {
      super(tokenRange);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public EmptyStmt clone() {
      return (EmptyStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public EmptyStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.emptyStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isEmptyStmt() {
      return true;
   }

   public EmptyStmt asEmptyStmt() {
      return this;
   }

   public void ifEmptyStmt(Consumer<EmptyStmt> action) {
      action.accept(this);
   }

   public Optional<EmptyStmt> toEmptyStmt() {
      return Optional.of(this);
   }
}
