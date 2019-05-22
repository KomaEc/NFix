package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.UnparsableStmtMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public final class UnparsableStmt extends Statement {
   @AllFieldsConstructor
   public UnparsableStmt() {
      this((TokenRange)null);
   }

   public UnparsableStmt(TokenRange tokenRange) {
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

   public UnparsableStmt clone() {
      return (UnparsableStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public UnparsableStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.unparsableStmtMetaModel;
   }

   public Node.Parsedness getParsed() {
      return Node.Parsedness.UNPARSABLE;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isUnparsableStmt() {
      return true;
   }

   public UnparsableStmt asUnparsableStmt() {
      return this;
   }

   public void ifUnparsableStmt(Consumer<UnparsableStmt> action) {
      action.accept(this);
   }

   public Optional<UnparsableStmt> toUnparsableStmt() {
      return Optional.of(this);
   }
}
