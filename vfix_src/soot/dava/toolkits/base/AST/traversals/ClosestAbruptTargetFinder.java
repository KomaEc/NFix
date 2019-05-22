package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
import java.util.HashMap;
import soot.G;
import soot.Singletons;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;

public class ClosestAbruptTargetFinder extends DepthFirstAdapter {
   HashMap<DAbruptStmt, ASTNode> closestNode = new HashMap();
   ArrayList<ASTLabeledNode> nodeStack = new ArrayList();

   public ClosestAbruptTargetFinder(Singletons.Global g) {
   }

   public static ClosestAbruptTargetFinder v() {
      return G.v().soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder();
   }

   public ASTNode getTarget(DAbruptStmt ab) {
      Object node = this.closestNode.get(ab);
      if (node != null) {
         return (ASTNode)node;
      } else {
         throw new RuntimeException("Unable to find target for AbruptStmt");
      }
   }

   public void inASTWhileNode(ASTWhileNode node) {
      this.nodeStack.add(node);
   }

   public void inASTDoWhileNode(ASTDoWhileNode node) {
      this.nodeStack.add(node);
   }

   public void inASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      this.nodeStack.add(node);
   }

   public void inASTForLoopNode(ASTForLoopNode node) {
      this.nodeStack.add(node);
   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      this.nodeStack.add(node);
   }

   public void outASTWhileNode(ASTWhileNode node) {
      if (this.nodeStack.isEmpty()) {
         throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
      } else {
         this.nodeStack.remove(this.nodeStack.size() - 1);
      }
   }

   public void outASTDoWhileNode(ASTDoWhileNode node) {
      if (this.nodeStack.isEmpty()) {
         throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
      } else {
         this.nodeStack.remove(this.nodeStack.size() - 1);
      }
   }

   public void outASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      if (this.nodeStack.isEmpty()) {
         throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
      } else {
         this.nodeStack.remove(this.nodeStack.size() - 1);
      }
   }

   public void outASTForLoopNode(ASTForLoopNode node) {
      if (this.nodeStack.isEmpty()) {
         throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
      } else {
         this.nodeStack.remove(this.nodeStack.size() - 1);
      }
   }

   public void outASTSwitchNode(ASTSwitchNode node) {
      if (this.nodeStack.isEmpty()) {
         throw new RuntimeException("trying to remove node from empty stack: ClosestBreakTargetFinder");
      } else {
         this.nodeStack.remove(this.nodeStack.size() - 1);
      }
   }

   public void inStmt(Stmt s) {
      if (s instanceof DAbruptStmt) {
         DAbruptStmt ab = (DAbruptStmt)s;
         SETNodeLabel label = ab.getLabel();
         if (label != null && label.toString() != null) {
            return;
         }

         int index;
         ASTNode currentNode;
         if (ab.is_Break()) {
            index = this.nodeStack.size() - 1;
            if (index < 0) {
               throw new RuntimeException("nodeStack empty??" + this.nodeStack.toString());
            }

            currentNode = (ASTNode)this.nodeStack.get(this.nodeStack.size() - 1);
            this.closestNode.put(ab, currentNode);
         } else if (ab.is_Continue()) {
            index = this.nodeStack.size() - 1;
            if (index < 0) {
               throw new RuntimeException("nodeStack empty??" + this.nodeStack.toString());
            }

            for(currentNode = (ASTNode)this.nodeStack.get(index); currentNode instanceof ASTSwitchNode; currentNode = (ASTNode)this.nodeStack.get(index)) {
               if (index <= 0) {
                  throw new RuntimeException("Unable to find closest break Target");
               }

               --index;
            }

            this.closestNode.put(ab, currentNode);
         }
      }

   }
}
