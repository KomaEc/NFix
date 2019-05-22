package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.BlockStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class BlockStmt extends Statement implements NodeWithStatements<BlockStmt> {
   private NodeList<Statement> statements;

   public BlockStmt() {
      this((TokenRange)null, new NodeList());
   }

   @AllFieldsConstructor
   public BlockStmt(final NodeList<Statement> statements) {
      this((TokenRange)null, statements);
   }

   public BlockStmt(TokenRange tokenRange, NodeList<Statement> statements) {
      super(tokenRange);
      this.setStatements(statements);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<Statement> getStatements() {
      return this.statements;
   }

   public BlockStmt setStatements(final NodeList<Statement> statements) {
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

   public BlockStmt clone() {
      return (BlockStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public BlockStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.blockStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
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

   public boolean isBlockStmt() {
      return true;
   }

   public BlockStmt asBlockStmt() {
      return this;
   }

   public void ifBlockStmt(Consumer<BlockStmt> action) {
      action.accept(this);
   }

   public Optional<BlockStmt> toBlockStmt() {
      return Optional.of(this);
   }
}
