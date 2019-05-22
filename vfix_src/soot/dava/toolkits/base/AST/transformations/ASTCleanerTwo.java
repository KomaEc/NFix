package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
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

public class ASTCleanerTwo extends DepthFirstAdapter {
   public ASTCleanerTwo() {
   }

   public ASTCleanerTwo(boolean verbose) {
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
            List<Object> subBody = (List)sbit.next();
            Iterator<Object> it = subBody.iterator();

            for(int nodeNumber = 0; it.hasNext(); ++nodeNumber) {
               ASTNode temp = (ASTNode)it.next();
               if (temp instanceof ASTIfElseNode) {
                  IfElseBreaker breaker = new IfElseBreaker();
                  boolean success = false;
                  if (breaker.isIfElseBreakingPossiblePatternOne((ASTIfElseNode)temp)) {
                     success = true;
                  } else if (breaker.isIfElseBreakingPossiblePatternTwo((ASTIfElseNode)temp)) {
                     success = true;
                  }

                  if (!success) {
                  }

                  if (success) {
                     List<Object> newBody = breaker.createNewBody(subBody, nodeNumber);
                     if (newBody != null) {
                        if (node instanceof ASTIfElseNode) {
                           List subBodies;
                           List ifBody;
                           if (subBodyNumber == 0) {
                              subBodies = node.get_SubBodies();
                              ifBody = (List)subBodies.get(1);
                              ((ASTIfElseNode)node).replaceBody(newBody, ifBody);
                              G.v().ASTTransformations_modified = true;
                              return;
                           }

                           if (subBodyNumber == 1) {
                              subBodies = node.get_SubBodies();
                              ifBody = (List)subBodies.get(0);
                              ((ASTIfElseNode)node).replaceBody(ifBody, newBody);
                              G.v().ASTTransformations_modified = true;
                              return;
                           }

                           throw new RuntimeException("Please report benchmark to programmer");
                        }

                        if (node instanceof ASTMethodNode) {
                           ((ASTMethodNode)node).replaceBody(newBody);
                           G.v().ASTTransformations_modified = true;
                           return;
                        }

                        if (node instanceof ASTSynchronizedBlockNode) {
                           ((ASTSynchronizedBlockNode)node).replaceBody(newBody);
                           G.v().ASTTransformations_modified = true;
                           return;
                        }

                        if (node instanceof ASTLabeledBlockNode) {
                           ((ASTLabeledBlockNode)node).replaceBody(newBody);
                           G.v().ASTTransformations_modified = true;
                           return;
                        }

                        if (node instanceof ASTUnconditionalLoopNode) {
                           ((ASTUnconditionalLoopNode)node).replaceBody(newBody);
                           G.v().ASTTransformations_modified = true;
                           return;
                        }

                        if (node instanceof ASTIfNode) {
                           ((ASTIfNode)node).replaceBody(newBody);
                           G.v().ASTTransformations_modified = true;
                           return;
                        }

                        if (node instanceof ASTWhileNode) {
                           ((ASTWhileNode)node).replaceBody(newBody);
                           G.v().ASTTransformations_modified = true;
                           return;
                        }

                        if (node instanceof ASTDoWhileNode) {
                           ((ASTDoWhileNode)node).replaceBody(newBody);
                           G.v().ASTTransformations_modified = true;
                           return;
                        }

                        if (node instanceof ASTForLoopNode) {
                           ((ASTForLoopNode)node).replaceBody(newBody);
                           G.v().ASTTransformations_modified = true;
                           return;
                        }

                        throw new RuntimeException("Please report benchmark to programmer");
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
      for(nodeNumber = 0; it.hasNext(); ++nodeNumber) {
         ASTNode temp = (ASTNode)it.next();
         if (temp instanceof ASTIfElseNode) {
            IfElseBreaker breaker = new IfElseBreaker();
            boolean success = false;
            if (breaker.isIfElseBreakingPossiblePatternOne((ASTIfElseNode)temp)) {
               success = true;
            } else if (breaker.isIfElseBreakingPossiblePatternTwo((ASTIfElseNode)temp)) {
               success = true;
            }

            if (G.v().ASTTransformations_modified) {
               return;
            }

            if (success) {
               newBody = breaker.createNewBody(tryBody, nodeNumber);
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
      List<Object> catchList = node.get_CatchList();
      newBody = null;
      it = catchList.iterator();

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
            if (temp instanceof ASTIfElseNode) {
               IfElseBreaker breaker = new IfElseBreaker();
               boolean success = false;
               if (breaker.isIfElseBreakingPossiblePatternOne((ASTIfElseNode)temp)) {
                  success = true;
               } else if (breaker.isIfElseBreakingPossiblePatternTwo((ASTIfElseNode)temp)) {
                  success = true;
               }

               if (G.v().ASTTransformations_modified) {
                  return;
               }

               if (success) {
                  List<Object> newBody = breaker.createNewBody(body, nodeNumber);
                  if (newBody != null) {
                     catchBody.replaceBody(newBody);
                     G.v().ASTTransformations_modified = true;
                     return;
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
            if (temp instanceof ASTIfElseNode) {
               IfElseBreaker breaker = new IfElseBreaker();
               boolean success = false;
               if (breaker.isIfElseBreakingPossiblePatternOne((ASTIfElseNode)temp)) {
                  success = true;
               } else if (breaker.isIfElseBreakingPossiblePatternTwo((ASTIfElseNode)temp)) {
                  success = true;
               }

               if (G.v().ASTTransformations_modified) {
                  return;
               }

               if (success) {
                  List<Object> newBody = breaker.createNewBody(body, nodeNumber);
                  if (newBody != null) {
                     index2BodyList.put(currentIndex, newBody);
                     node.replaceIndex2BodyList(index2BodyList);
                     G.v().ASTTransformations_modified = true;
                     return;
                  }
               }
            }

            temp.apply(this);
         }
      }
   }
}
