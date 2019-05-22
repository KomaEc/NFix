package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;

public class UselessLabeledBlockRemover extends DepthFirstAdapter {
   boolean changed = false;

   public UselessLabeledBlockRemover() {
   }

   public UselessLabeledBlockRemover(boolean verbose) {
      super(verbose);
   }

   public void outASTMethodNode(ASTMethodNode node) {
      if (this.changed) {
         G.v().ASTTransformations_modified = true;
      }

   }

   public void inASTMethodNode(ASTMethodNode node) {
      this.changed = UselessLabelFinder.v().findAndKill(node);
   }

   public void outASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTLabeledBlockNode(ASTLabeledBlockNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTSwitchNode(ASTSwitchNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTIfNode(ASTIfNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTIfElseNode(ASTIfElseNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTWhileNode(ASTWhileNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTForLoopNode(ASTForLoopNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTDoWhileNode(ASTDoWhileNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public void outASTTryNode(ASTTryNode node) {
      boolean modified = UselessLabelFinder.v().findAndKill(node);
      if (modified) {
         this.changed = true;
      }

   }

   public static void removeLabeledBlock(ASTNode node, ASTLabeledBlockNode labelBlock, int subBodyNumber, int nodeNumber) {
      List subBodies;
      List toModifySubBody;
      List newBody;
      if (!(node instanceof ASTIfElseNode)) {
         subBodies = node.get_SubBodies();
         if (subBodies.size() != 1) {
            throw new RuntimeException("Please report this benchmark to the programmer");
         }

         toModifySubBody = (List)subBodies.get(0);
         newBody = createNewSubBody(toModifySubBody, nodeNumber, labelBlock);
         if (newBody == null) {
            return;
         }

         if (node instanceof ASTMethodNode) {
            ((ASTMethodNode)node).replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         } else if (node instanceof ASTSynchronizedBlockNode) {
            ((ASTSynchronizedBlockNode)node).replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         } else if (node instanceof ASTLabeledBlockNode) {
            ((ASTLabeledBlockNode)node).replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         } else if (node instanceof ASTUnconditionalLoopNode) {
            ((ASTUnconditionalLoopNode)node).replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         } else if (node instanceof ASTIfNode) {
            ((ASTIfNode)node).replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         } else if (node instanceof ASTWhileNode) {
            ((ASTWhileNode)node).replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         } else {
            if (!(node instanceof ASTDoWhileNode)) {
               return;
            }

            ((ASTDoWhileNode)node).replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         }
      } else {
         if (subBodyNumber != 0 && subBodyNumber != 1) {
            return;
         }

         subBodies = node.get_SubBodies();
         if (subBodies.size() != 2) {
            throw new RuntimeException("Please report this benchmark to the programmer");
         }

         toModifySubBody = (List)subBodies.get(subBodyNumber);
         newBody = createNewSubBody(toModifySubBody, nodeNumber, labelBlock);
         if (newBody == null) {
            return;
         }

         if (subBodyNumber == 0) {
            G.v().ASTTransformations_modified = true;
            ((ASTIfElseNode)node).replaceBody(newBody, (List)subBodies.get(1));
         } else {
            if (subBodyNumber != 1) {
               return;
            }

            G.v().ASTTransformations_modified = true;
            ((ASTIfElseNode)node).replaceBody((List)subBodies.get(0), newBody);
         }
      }

   }

   public static List<Object> createNewSubBody(List<Object> oldSubBody, int nodeNumber, ASTLabeledBlockNode labelBlock) {
      List<Object> newSubBody = new ArrayList();
      Iterator<Object> it = oldSubBody.iterator();

      for(int index = 0; index != nodeNumber; ++index) {
         if (!it.hasNext()) {
            return null;
         }

         newSubBody.add(it.next());
      }

      ASTNode toRemove = (ASTNode)it.next();
      if (!(toRemove instanceof ASTLabeledBlockNode)) {
         return null;
      } else {
         ASTLabeledBlockNode toRemoveNode = (ASTLabeledBlockNode)toRemove;
         SETNodeLabel label = toRemoveNode.get_Label();
         if (label.toString() != null) {
            return null;
         } else {
            List<Object> blocksSubBodies = toRemoveNode.get_SubBodies();
            List onlySubBodyOfLabeledBlock = (List)blocksSubBodies.get(0);
            newSubBody.addAll(onlySubBodyOfLabeledBlock);

            while(it.hasNext()) {
               newSubBody.add(it.next());
            }

            return newSubBody;
         }
      }
   }
}
