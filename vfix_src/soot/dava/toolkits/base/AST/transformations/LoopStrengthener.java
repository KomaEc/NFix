package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;

public class LoopStrengthener extends DepthFirstAdapter {
   public LoopStrengthener() {
   }

   public LoopStrengthener(boolean verbose) {
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
               if (temp instanceof ASTWhileNode || temp instanceof ASTUnconditionalLoopNode || temp instanceof ASTDoWhileNode) {
                  ASTNode oneNode = this.getOnlySubNode(temp);
                  if (oneNode != null) {
                     List<ASTNode> newNode = null;
                     if (oneNode instanceof ASTIfNode) {
                        newNode = StrengthenByIf.getNewNode(temp, (ASTIfNode)oneNode);
                     } else if (oneNode instanceof ASTIfElseNode) {
                        newNode = StrengthenByIfElse.getNewNode(temp, (ASTIfElseNode)oneNode);
                     }

                     if (newNode != null) {
                        this.replaceNode(node, subBodyNumber, nodeNumber, temp, newNode);
                        UselessLabelFinder.v().findAndKill(node);
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
      List newNode;
      List newBody;
      for(nodeNumber = 0; it.hasNext(); ++nodeNumber) {
         ASTNode temp = (ASTNode)it.next();
         if (temp instanceof ASTWhileNode || temp instanceof ASTUnconditionalLoopNode || temp instanceof ASTDoWhileNode) {
            ASTNode oneNode = this.getOnlySubNode(temp);
            if (oneNode != null) {
               newNode = null;
               if (oneNode instanceof ASTIfNode) {
                  newNode = StrengthenByIf.getNewNode(temp, (ASTIfNode)oneNode);
               } else if (oneNode instanceof ASTIfElseNode) {
                  newNode = StrengthenByIfElse.getNewNode(temp, (ASTIfElseNode)oneNode);
               }

               if (newNode != null) {
                  newBody = createNewSubBody(tryBody, nodeNumber, temp, newNode);
                  if (newBody != null) {
                     node.replaceTryBody(newBody);
                     G.v().ASTTransformations_modified = true;
                  }

                  UselessLabelFinder.v().findAndKill(node);
               }
            }
         }

         temp.apply(this);
      }

      Map<Object, Object> exceptionMap = node.get_ExceptionMap();
      Map<Object, Object> paramMap = node.get_ParamMap();
      newNode = node.get_CatchList();
      newBody = null;
      it = newNode.iterator();

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
            if (temp instanceof ASTWhileNode || temp instanceof ASTUnconditionalLoopNode || temp instanceof ASTDoWhileNode) {
               ASTNode oneNode = this.getOnlySubNode(temp);
               if (oneNode != null) {
                  List<ASTNode> newNode = null;
                  if (oneNode instanceof ASTIfNode) {
                     newNode = StrengthenByIf.getNewNode(temp, (ASTIfNode)oneNode);
                  } else if (oneNode instanceof ASTIfElseNode) {
                     newNode = StrengthenByIfElse.getNewNode(temp, (ASTIfElseNode)oneNode);
                  }

                  if (newNode != null) {
                     List<Object> newBody = createNewSubBody(body, nodeNumber, temp, newNode);
                     if (newBody != null) {
                        catchBody.replaceBody(newBody);
                        G.v().ASTTransformations_modified = true;
                     }

                     UselessLabelFinder.v().findAndKill(node);
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
            if (temp instanceof ASTWhileNode || temp instanceof ASTUnconditionalLoopNode || temp instanceof ASTDoWhileNode) {
               ASTNode oneNode = this.getOnlySubNode(temp);
               if (oneNode != null) {
                  List<ASTNode> newNode = null;
                  if (oneNode instanceof ASTIfNode) {
                     newNode = StrengthenByIf.getNewNode(temp, (ASTIfNode)oneNode);
                  } else if (oneNode instanceof ASTIfElseNode) {
                     newNode = StrengthenByIfElse.getNewNode(temp, (ASTIfElseNode)oneNode);
                  }

                  if (newNode != null) {
                     List<Object> newBody = createNewSubBody(body, nodeNumber, temp, newNode);
                     if (newBody != null) {
                        index2BodyList.put(currentIndex, newBody);
                        node.replaceIndex2BodyList(index2BodyList);
                        G.v().ASTTransformations_modified = true;
                     }

                     UselessLabelFinder.v().findAndKill(node);
                  }
               }
            }

            temp.apply(this);
         }
      }
   }

   private ASTNode getOnlySubNode(ASTNode node) {
      if (!(node instanceof ASTWhileNode) && !(node instanceof ASTDoWhileNode) && !(node instanceof ASTUnconditionalLoopNode)) {
         return null;
      } else {
         List<Object> subBodies = node.get_SubBodies();
         if (subBodies.size() != 1) {
            return null;
         } else {
            List subBody = (List)subBodies.get(0);
            return subBody.size() != 1 ? null : (ASTNode)subBody.get(0);
         }
      }
   }

   private void replaceNode(ASTNode node, int subBodyNumber, int nodeNumber, ASTNode loopNode, List<ASTNode> newNode) {
      List subBodies;
      List toModifySubBody;
      List newBody;
      if (!(node instanceof ASTIfElseNode)) {
         subBodies = node.get_SubBodies();
         if (subBodies.size() != 1) {
            throw new RuntimeException("Please report this benchmark to the programmer");
         }

         toModifySubBody = (List)subBodies.get(0);
         newBody = createNewSubBody(toModifySubBody, nodeNumber, loopNode, newNode);
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
         newBody = createNewSubBody(toModifySubBody, nodeNumber, loopNode, newNode);
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

   public static List<Object> createNewSubBody(List<Object> oldSubBody, int nodeNumber, ASTNode oldNode, List<ASTNode> newNode) {
      List<Object> newSubBody = new ArrayList();
      Iterator<Object> it = oldSubBody.iterator();

      for(int index = 0; index != nodeNumber; ++index) {
         if (!it.hasNext()) {
            return null;
         }

         newSubBody.add(it.next());
      }

      ASTNode toRemove = (ASTNode)it.next();
      if (toRemove.toString().compareTo(oldNode.toString()) != 0) {
         System.out.println("The replace nodes dont match please report benchmark to developer");
         return null;
      } else {
         newSubBody.addAll(newNode);

         while(it.hasNext()) {
            newSubBody.add(it.next());
         }

         return newSubBody;
      }
   }
}
