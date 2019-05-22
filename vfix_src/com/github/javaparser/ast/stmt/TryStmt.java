package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.metamodel.TryStmtMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class TryStmt extends Statement {
   private NodeList<Expression> resources;
   private BlockStmt tryBlock;
   private NodeList<CatchClause> catchClauses;
   @OptionalProperty
   private BlockStmt finallyBlock;

   public TryStmt() {
      this((TokenRange)null, new NodeList(), new BlockStmt(), new NodeList(), (BlockStmt)null);
   }

   public TryStmt(final BlockStmt tryBlock, final NodeList<CatchClause> catchClauses, final BlockStmt finallyBlock) {
      this((TokenRange)null, new NodeList(), tryBlock, catchClauses, finallyBlock);
   }

   @AllFieldsConstructor
   public TryStmt(NodeList<Expression> resources, final BlockStmt tryBlock, final NodeList<CatchClause> catchClauses, final BlockStmt finallyBlock) {
      this((TokenRange)null, resources, tryBlock, catchClauses, finallyBlock);
   }

   public TryStmt(TokenRange tokenRange, NodeList<Expression> resources, BlockStmt tryBlock, NodeList<CatchClause> catchClauses, BlockStmt finallyBlock) {
      super(tokenRange);
      this.setResources(resources);
      this.setTryBlock(tryBlock);
      this.setCatchClauses(catchClauses);
      this.setFinallyBlock(finallyBlock);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<CatchClause> getCatchClauses() {
      return this.catchClauses;
   }

   public Optional<BlockStmt> getFinallyBlock() {
      return Optional.ofNullable(this.finallyBlock);
   }

   public BlockStmt getTryBlock() {
      return this.tryBlock;
   }

   public NodeList<Expression> getResources() {
      return this.resources;
   }

   public TryStmt setCatchClauses(final NodeList<CatchClause> catchClauses) {
      Utils.assertNotNull(catchClauses);
      if (catchClauses == this.catchClauses) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.CATCH_CLAUSES, this.catchClauses, catchClauses);
         if (this.catchClauses != null) {
            this.catchClauses.setParentNode((Node)null);
         }

         this.catchClauses = catchClauses;
         this.setAsParentNodeOf(catchClauses);
         return this;
      }
   }

   public TryStmt setFinallyBlock(final BlockStmt finallyBlock) {
      if (finallyBlock == this.finallyBlock) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.FINALLY_BLOCK, this.finallyBlock, finallyBlock);
         if (this.finallyBlock != null) {
            this.finallyBlock.setParentNode((Node)null);
         }

         this.finallyBlock = finallyBlock;
         this.setAsParentNodeOf(finallyBlock);
         return this;
      }
   }

   public TryStmt setTryBlock(final BlockStmt tryBlock) {
      Utils.assertNotNull(tryBlock);
      if (tryBlock == this.tryBlock) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TRY_BLOCK, this.tryBlock, tryBlock);
         if (this.tryBlock != null) {
            this.tryBlock.setParentNode((Node)null);
         }

         this.tryBlock = tryBlock;
         this.setAsParentNodeOf(tryBlock);
         return this;
      }
   }

   public TryStmt setResources(final NodeList<Expression> resources) {
      Utils.assertNotNull(resources);
      if (resources == this.resources) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.RESOURCES, this.resources, resources);
         if (this.resources != null) {
            this.resources.setParentNode((Node)null);
         }

         this.resources = resources;
         this.setAsParentNodeOf(resources);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.catchClauses.size(); ++i) {
            if (this.catchClauses.get(i) == node) {
               this.catchClauses.remove(i);
               return true;
            }
         }

         if (this.finallyBlock != null && node == this.finallyBlock) {
            this.removeFinallyBlock();
            return true;
         } else {
            for(i = 0; i < this.resources.size(); ++i) {
               if (this.resources.get(i) == node) {
                  this.resources.remove(i);
                  return true;
               }
            }

            return super.remove(node);
         }
      }
   }

   public TryStmt removeFinallyBlock() {
      return this.setFinallyBlock((BlockStmt)null);
   }

   public TryStmt removeTryBlock() {
      return this.setTryBlock((BlockStmt)null);
   }

   public TryStmt clone() {
      return (TryStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public TryStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.tryStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.catchClauses.size(); ++i) {
            if (this.catchClauses.get(i) == node) {
               this.catchClauses.set(i, (Node)((CatchClause)replacementNode));
               return true;
            }
         }

         if (this.finallyBlock != null && node == this.finallyBlock) {
            this.setFinallyBlock((BlockStmt)replacementNode);
            return true;
         } else {
            for(i = 0; i < this.resources.size(); ++i) {
               if (this.resources.get(i) == node) {
                  this.resources.set(i, (Node)((Expression)replacementNode));
                  return true;
               }
            }

            if (node == this.tryBlock) {
               this.setTryBlock((BlockStmt)replacementNode);
               return true;
            } else {
               return super.replace(node, replacementNode);
            }
         }
      }
   }

   public boolean isTryStmt() {
      return true;
   }

   public TryStmt asTryStmt() {
      return this;
   }

   public void ifTryStmt(Consumer<TryStmt> action) {
      action.accept(this);
   }

   public Optional<TryStmt> toTryStmt() {
      return Optional.of(this);
   }
}
