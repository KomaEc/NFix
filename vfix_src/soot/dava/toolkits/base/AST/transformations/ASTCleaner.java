package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;

public class ASTCleaner extends DepthFirstAdapter {
   public ASTCleaner() {
   }

   public ASTCleaner(boolean verbose) {
      super(verbose);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
   }

   public void normalRetrieving(ASTNode node) {
      if (node instanceof ASTSwitchNode) {
         this.dealWithSwitchNode((ASTSwitchNode)node);
      } else {
         Iterator<Object> sbit = node.get_SubBodies().iterator();

         for(int subBodyNumber = 0; sbit.hasNext(); ++subBodyNumber) {
            Object subBody = sbit.next();
            Iterator it = ((List)subBody).iterator();

            for(int nodeNumber = 0; it.hasNext(); ++nodeNumber) {
               ASTNode temp = (ASTNode)it.next();
               if (temp instanceof ASTLabeledBlockNode) {
                  ASTLabeledBlockNode labelBlock = (ASTLabeledBlockNode)temp;
                  SETNodeLabel label = labelBlock.get_Label();
                  if (label.toString() == null) {
                     UselessLabeledBlockRemover.removeLabeledBlock(node, labelBlock, subBodyNumber, nodeNumber);
                     if (G.v().ASTTransformations_modified) {
                        return;
                     }
                  }
               } else if (temp instanceof ASTIfElseNode) {
                  List<Object> elseBody = ((ASTIfElseNode)temp).getElseBody();
                  if (elseBody.size() == 0) {
                     EmptyElseRemover.removeElseBody(node, (ASTIfElseNode)temp, subBodyNumber, nodeNumber);
                  }
               } else if (temp instanceof ASTIfNode && it.hasNext()) {
                  ASTNode nextNode = (ASTNode)((List)subBody).get(nodeNumber + 1);
                  if (nextNode instanceof ASTIfNode) {
                     OrAggregatorThree.checkAndTransform(node, (ASTIfNode)temp, (ASTIfNode)nextNode, nodeNumber, subBodyNumber);
                     if (G.v().ASTTransformations_modified) {
                        return;
                     }
                  }
               }

               temp.apply(this);
            }
         }

      }
   }

   public void caseASTTryNode(ASTTryNode node) {
      this.inASTTryNode(node);
      List<Object> tryBody = node.get_TryBody();
      Iterator<Object> it = tryBody.iterator();

      int nodeNumber;
      List newBody;
      List newBody;
      for(nodeNumber = 0; it.hasNext(); ++nodeNumber) {
         ASTNode temp = (ASTNode)it.next();
         if (temp instanceof ASTLabeledBlockNode) {
            ASTLabeledBlockNode labelBlock = (ASTLabeledBlockNode)temp;
            SETNodeLabel label = labelBlock.get_Label();
            if (label.toString() == null) {
               newBody = UselessLabeledBlockRemover.createNewSubBody(tryBody, nodeNumber, labelBlock);
               if (newBody != null) {
                  node.replaceTryBody(newBody);
                  G.v().ASTTransformations_modified = true;
               }
            }
         } else if (temp instanceof ASTIfElseNode) {
            List<Object> elseBody = ((ASTIfElseNode)temp).getElseBody();
            if (elseBody.size() == 0) {
               newBody = EmptyElseRemover.createNewNodeBody(tryBody, nodeNumber, (ASTIfElseNode)temp);
               if (newBody != null) {
                  node.replaceTryBody(newBody);
                  G.v().ASTTransformations_modified = true;
                  return;
               }
            }
         } else if (temp instanceof ASTIfNode && it.hasNext()) {
            ASTNode nextNode = (ASTNode)tryBody.get(nodeNumber + 1);
            if (nextNode instanceof ASTIfNode) {
               newBody = OrAggregatorThree.createNewNodeBody(tryBody, nodeNumber, (ASTIfNode)temp, (ASTIfNode)nextNode);
               if (newBody != null) {
                  node.replaceTryBody(newBody);
                  G.v().ASTTransformations_modified = true;
                  return;
               }
            }
         }

         temp.apply(this);
      }

      Map<Object, Object> exceptionMap = node.get_ExceptionMap();
      Map<Object, Object> paramMap = node.get_ParamMap();
      newBody = node.get_CatchList();
      newBody = null;
      it = newBody.iterator();

      while(it.hasNext()) {
         ASTTryNode.container catchBody = (ASTTryNode.container)it.next();
         SootClass sootClass = (SootClass)exceptionMap.get(catchBody);
         Type type = sootClass.getType();
         this.caseType(type);
         Local local = (Local)paramMap.get(catchBody);
         this.decideCaseExprOrRef(local);
         List<Object> body = (List)catchBody.o;
         Iterator<Object> itBody = body.iterator();

         for(nodeNumber = 0; itBody.hasNext(); ++nodeNumber) {
            ASTNode temp = (ASTNode)itBody.next();
            if (temp instanceof ASTLabeledBlockNode) {
               ASTLabeledBlockNode labelBlock = (ASTLabeledBlockNode)temp;
               SETNodeLabel label = labelBlock.get_Label();
               if (label.toString() == null) {
                  List<Object> newBody = UselessLabeledBlockRemover.createNewSubBody(body, nodeNumber, labelBlock);
                  if (newBody != null) {
                     catchBody.replaceBody(newBody);
                     G.v().ASTTransformations_modified = true;
                  }
               }
            } else {
               List newBody;
               if (temp instanceof ASTIfElseNode) {
                  List<Object> elseBody = ((ASTIfElseNode)temp).getElseBody();
                  if (elseBody.size() == 0) {
                     newBody = EmptyElseRemover.createNewNodeBody(body, nodeNumber, (ASTIfElseNode)temp);
                     if (newBody != null) {
                        catchBody.replaceBody(newBody);
                        G.v().ASTTransformations_modified = true;
                        return;
                     }
                  }
               } else if (temp instanceof ASTIfNode && itBody.hasNext()) {
                  ASTNode nextNode = (ASTNode)body.get(nodeNumber + 1);
                  if (nextNode instanceof ASTIfNode) {
                     newBody = OrAggregatorThree.createNewNodeBody(body, nodeNumber, (ASTIfNode)temp, (ASTIfNode)nextNode);
                     if (newBody != null) {
                        catchBody.replaceBody(newBody);
                        G.v().ASTTransformations_modified = true;
                        return;
                     }
                  }
               }
            }

            temp.apply(this);
         }
      }

      this.outASTTryNode(node);
   }

   private void dealWithSwitchNode(ASTSwitchNode node) {
      List<Object> indexList = node.getIndexList();
      Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
      Iterator it = indexList.iterator();

      while(true) {
         Object currentIndex;
         List body;
         do {
            if (!it.hasNext()) {
               return;
            }

            currentIndex = it.next();
            body = (List)index2BodyList.get(currentIndex);
         } while(body == null);

         Iterator<Object> itBody = body.iterator();

         for(int nodeNumber = 0; itBody.hasNext(); ++nodeNumber) {
            ASTNode temp = (ASTNode)itBody.next();
            if (temp instanceof ASTLabeledBlockNode) {
               ASTLabeledBlockNode labelBlock = (ASTLabeledBlockNode)temp;
               SETNodeLabel label = labelBlock.get_Label();
               if (label.toString() == null) {
                  List<Object> newBody = UselessLabeledBlockRemover.createNewSubBody(body, nodeNumber, labelBlock);
                  if (newBody != null) {
                     index2BodyList.put(currentIndex, newBody);
                     node.replaceIndex2BodyList(index2BodyList);
                     G.v().ASTTransformations_modified = true;
                  }
               }
            } else {
               List newBody;
               if (temp instanceof ASTIfElseNode) {
                  List<Object> elseBody = ((ASTIfElseNode)temp).getElseBody();
                  if (elseBody.size() == 0) {
                     newBody = EmptyElseRemover.createNewNodeBody(body, nodeNumber, (ASTIfElseNode)temp);
                     if (newBody != null) {
                        index2BodyList.put(currentIndex, newBody);
                        node.replaceIndex2BodyList(index2BodyList);
                        G.v().ASTTransformations_modified = true;
                        return;
                     }
                  }
               } else if (temp instanceof ASTIfNode && itBody.hasNext()) {
                  ASTNode nextNode = (ASTNode)body.get(nodeNumber + 1);
                  if (nextNode instanceof ASTIfNode) {
                     newBody = OrAggregatorThree.createNewNodeBody(body, nodeNumber, (ASTIfNode)temp, (ASTIfNode)nextNode);
                     if (newBody != null) {
                        index2BodyList.put(currentIndex, newBody);
                        node.replaceIndex2BodyList(index2BodyList);
                        G.v().ASTTransformations_modified = true;
                        return;
                     }
                  }
               }
            }

            temp.apply(this);
         }
      }
   }
}
