package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;

public class EmptyElseRemover {
   public static void removeElseBody(ASTNode node, ASTIfElseNode ifElseNode, int subBodyNumber, int nodeNumber) {
      List subBodies;
      List toModifySubBody;
      List newBody;
      if (!(node instanceof ASTIfElseNode)) {
         subBodies = node.get_SubBodies();
         if (subBodies.size() != 1) {
            throw new RuntimeException("Please report this benchmark to the programmer");
         }

         toModifySubBody = (List)subBodies.get(0);
         newBody = createNewNodeBody(toModifySubBody, nodeNumber, ifElseNode);
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
         newBody = createNewNodeBody(toModifySubBody, nodeNumber, ifElseNode);
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

   public static List<Object> createNewNodeBody(List<Object> oldSubBody, int nodeNumber, ASTIfElseNode ifElseNode) {
      List<Object> newSubBody = new ArrayList();
      Iterator<Object> it = oldSubBody.iterator();

      for(int index = 0; index != nodeNumber; ++index) {
         if (!it.hasNext()) {
            return null;
         }

         newSubBody.add(it.next());
      }

      ASTNode toRemove = (ASTNode)it.next();
      if (!(toRemove instanceof ASTIfElseNode)) {
         return null;
      } else {
         ASTIfElseNode toRemoveNode = (ASTIfElseNode)toRemove;
         List<Object> elseBody = toRemoveNode.getElseBody();
         if (elseBody.size() != 0) {
            return null;
         } else {
            ASTIfNode newNode = new ASTIfNode(toRemoveNode.get_Label(), toRemoveNode.get_Condition(), toRemoveNode.getIfBody());
            newSubBody.add(newNode);

            while(it.hasNext()) {
               newSubBody.add(it.next());
            }

            return newSubBody;
         }
      }
   }
}
