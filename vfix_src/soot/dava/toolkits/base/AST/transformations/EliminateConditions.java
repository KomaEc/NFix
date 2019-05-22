package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.BooleanType;
import soot.Value;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTControlFlowNode;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DNotExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;

public class EliminateConditions extends DepthFirstAdapter {
   public static boolean DEBUG = false;
   public boolean modified = false;
   ASTParentNodeFinder finder = new ASTParentNodeFinder();
   ASTMethodNode AST;
   List<Object> bodyContainingNode = null;

   public EliminateConditions(ASTMethodNode AST) {
      this.AST = AST;
   }

   public EliminateConditions(boolean verbose, ASTMethodNode AST) {
      super(verbose);
      this.AST = AST;
   }

   public void normalRetrieving(ASTNode node) {
      this.modified = false;
      if (node instanceof ASTSwitchNode) {
         do {
            this.modified = false;
            this.dealWithSwitchNode((ASTSwitchNode)node);
         } while(this.modified);

      } else {
         Iterator sbit = node.get_SubBodies().iterator();

         while(sbit.hasNext()) {
            List subBody = (List)sbit.next();
            Iterator it = subBody.iterator();
            ASTNode temp = null;

            Boolean returned;
            for(returned = null; it.hasNext(); temp.apply(this)) {
               temp = (ASTNode)it.next();
               if (temp instanceof ASTControlFlowNode) {
                  this.bodyContainingNode = null;
                  returned = this.eliminate(temp);
                  if (returned != null && this.canChange(returned, temp)) {
                     break;
                  }

                  if (DEBUG) {
                     System.out.println("returned is null" + temp.getClass());
                  }

                  this.bodyContainingNode = null;
               }
            }

            boolean changed = this.change(returned, temp);
            if (changed) {
               this.modified = true;
            }
         }

         if (this.modified) {
            this.normalRetrieving(node);
         }

      }
   }

   public Boolean eliminate(ASTNode node) {
      ASTCondition cond = null;
      if (node instanceof ASTControlFlowNode) {
         cond = ((ASTControlFlowNode)node).get_Condition();
         if (cond != null && cond instanceof ASTUnaryCondition) {
            ASTUnaryCondition unary = (ASTUnaryCondition)cond;
            Value unaryValue = unary.getValue();
            boolean notted = false;
            if (unaryValue instanceof DNotExpr) {
               notted = true;
               unaryValue = ((DNotExpr)unaryValue).getOp();
            }

            Boolean isBoolean = this.isBooleanConstant(unaryValue);
            if (isBoolean == null) {
               return null;
            } else {
               boolean trueOrFalse = isBoolean;
               if (notted) {
                  trueOrFalse = !trueOrFalse;
               }

               this.AST.apply(this.finder);
               Object temp = this.finder.getParentOf(node);
               if (temp == null) {
                  return null;
               } else {
                  ASTNode parent = (ASTNode)temp;
                  List<Object> subBodies = parent.get_SubBodies();
                  Iterator<Object> it = subBodies.iterator();

                  for(boolean var12 = true; it.hasNext(); this.bodyContainingNode = null) {
                     this.bodyContainingNode = (List)it.next();
                     int index = this.bodyContainingNode.indexOf(node);
                     if (index >= 0) {
                        return new Boolean(trueOrFalse);
                     }
                  }

                  return null;
               }
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public Boolean isBooleanConstant(Value internal) {
      if (!(internal instanceof DIntConstant)) {
         return null;
      } else {
         if (DEBUG) {
            System.out.println("Found Constant");
         }

         DIntConstant intConst = (DIntConstant)internal;
         if (!(intConst.type instanceof BooleanType)) {
            return null;
         } else {
            if (DEBUG) {
               System.out.println("Found Boolean Constant");
            }

            if (intConst.value == 1) {
               return new Boolean(true);
            } else if (intConst.value == 0) {
               return new Boolean(false);
            } else {
               throw new RuntimeException("BooleanType found with value different than 0 or 1");
            }
         }
      }
   }

   public Boolean eliminateForTry(ASTNode node) {
      ASTCondition cond = null;
      if (node instanceof ASTControlFlowNode) {
         cond = ((ASTControlFlowNode)node).get_Condition();
         if (cond != null && cond instanceof ASTUnaryCondition) {
            ASTUnaryCondition unary = (ASTUnaryCondition)cond;
            Value unaryValue = unary.getValue();
            boolean notted = false;
            if (unaryValue instanceof DNotExpr) {
               notted = true;
               unaryValue = ((DNotExpr)unaryValue).getOp();
            }

            Boolean isBoolean = this.isBooleanConstant(unaryValue);
            if (isBoolean == null) {
               return null;
            } else {
               boolean trueOrFalse = isBoolean;
               if (notted) {
                  trueOrFalse = !trueOrFalse;
               }

               this.AST.apply(this.finder);
               Object temp = this.finder.getParentOf(node);
               if (temp == null) {
                  return null;
               } else if (!(temp instanceof ASTTryNode)) {
                  throw new RuntimeException("eliminateTry called when parent was not a try node");
               } else {
                  ASTTryNode parent = (ASTTryNode)temp;
                  List<Object> tryBody = parent.get_TryBody();
                  int index = tryBody.indexOf(node);
                  if (index >= 0) {
                     this.bodyContainingNode = tryBody;
                     return new Boolean(trueOrFalse);
                  } else {
                     List<Object> catchList = parent.get_CatchList();
                     Iterator it = catchList.iterator();

                     List body;
                     do {
                        if (!it.hasNext()) {
                           return null;
                        }

                        ASTTryNode.container catchBody = (ASTTryNode.container)it.next();
                        body = (List)catchBody.o;
                        index = body.indexOf(node);
                     } while(index < 0);

                     this.bodyContainingNode = body;
                     return new Boolean(trueOrFalse);
                  }
               }
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public void caseASTTryNode(ASTTryNode node) {
      this.modified = false;
      this.inASTTryNode(node);
      Iterator<Object> it = node.get_TryBody().iterator();
      Boolean returned = null;

      ASTNode temp;
      for(temp = null; it.hasNext(); temp.apply(this)) {
         temp = (ASTNode)it.next();
         if (temp instanceof ASTControlFlowNode) {
            this.bodyContainingNode = null;
            returned = this.eliminateForTry(temp);
            if (returned != null && this.canChange(returned, temp)) {
               break;
            }

            this.bodyContainingNode = null;
         }
      }

      boolean changed = this.change(returned, temp);
      if (changed) {
         this.modified = true;
      }

      List<Object> catchList = node.get_CatchList();
      Iterator itBody = null;
      it = catchList.iterator();

      while(it.hasNext()) {
         ASTTryNode.container catchBody = (ASTTryNode.container)it.next();
         List body = (List)catchBody.o;
         itBody = body.iterator();
         returned = null;

         for(temp = null; itBody.hasNext(); temp.apply(this)) {
            temp = (ASTNode)itBody.next();
            if (temp instanceof ASTControlFlowNode) {
               this.bodyContainingNode = null;
               returned = this.eliminateForTry(temp);
               if (returned != null && this.canChange(returned, temp)) {
                  break;
               }

               this.bodyContainingNode = null;
            }
         }

         changed = this.change(returned, temp);
         if (changed) {
            this.modified = true;
         }
      }

      this.outASTTryNode(node);
      if (this.modified) {
         this.caseASTTryNode(node);
      }

   }

   public boolean canChange(Boolean returned, ASTNode temp) {
      return true;
   }

   public boolean change(Boolean returned, ASTNode temp) {
      if (this.bodyContainingNode != null && returned != null && temp != null) {
         int index = this.bodyContainingNode.indexOf(temp);
         if (DEBUG) {
            System.out.println("in change");
         }

         ASTLabeledBlockNode labeledNode;
         String label;
         if (temp instanceof ASTIfNode) {
            this.bodyContainingNode.remove(temp);
            if (returned) {
               label = ((ASTLabeledNode)temp).get_Label().toString();
               if (label != null) {
                  labeledNode = new ASTLabeledBlockNode(((ASTLabeledNode)temp).get_Label(), (List)temp.get_SubBodies().get(0));
                  this.bodyContainingNode.add(index, labeledNode);
               } else {
                  this.bodyContainingNode.addAll(index, (List)temp.get_SubBodies().get(0));
               }
            }

            if (DEBUG) {
               System.out.println("Removed if" + temp);
            }

            return true;
         }

         if (temp instanceof ASTIfElseNode) {
            this.bodyContainingNode.remove(temp);
            if (returned) {
               label = ((ASTLabeledNode)temp).get_Label().toString();
               if (label != null) {
                  labeledNode = new ASTLabeledBlockNode(((ASTLabeledNode)temp).get_Label(), (List)temp.get_SubBodies().get(0));
                  this.bodyContainingNode.add(index, labeledNode);
               } else {
                  this.bodyContainingNode.addAll(index, (List)temp.get_SubBodies().get(0));
               }
            } else {
               label = ((ASTLabeledNode)temp).get_Label().toString();
               if (label != null) {
                  labeledNode = new ASTLabeledBlockNode(((ASTLabeledNode)temp).get_Label(), (List)temp.get_SubBodies().get(1));
                  this.bodyContainingNode.add(index, labeledNode);
               } else {
                  this.bodyContainingNode.addAll(index, (List)temp.get_SubBodies().get(1));
               }
            }

            return true;
         }

         if (temp instanceof ASTWhileNode && !returned) {
            this.bodyContainingNode.remove(temp);
            return true;
         }

         if (temp instanceof ASTDoWhileNode && !returned) {
            this.bodyContainingNode.remove(temp);
            this.bodyContainingNode.addAll(index, (List)temp.get_SubBodies().get(0));
            return true;
         }

         if (temp instanceof ASTForLoopNode && !returned) {
            this.bodyContainingNode.remove(temp);
            ASTStatementSequenceNode newNode = new ASTStatementSequenceNode(((ASTForLoopNode)temp).getInit());
            this.bodyContainingNode.add(index, newNode);
            return true;
         }
      }

      return false;
   }

   public void dealWithSwitchNode(ASTSwitchNode node) {
      List<Object> indexList = node.getIndexList();
      Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
      Iterator it = indexList.iterator();

      while(true) {
         List body;
         do {
            if (!it.hasNext()) {
               return;
            }

            Object currentIndex = it.next();
            body = (List)index2BodyList.get(currentIndex);
         } while(body == null);

         Iterator itBody = body.iterator();
         Boolean returned = null;

         ASTNode temp;
         for(temp = null; itBody.hasNext(); temp.apply(this)) {
            temp = (ASTNode)itBody.next();
            if (temp instanceof ASTControlFlowNode) {
               this.bodyContainingNode = null;
               returned = this.eliminate(temp);
               if (returned != null && this.canChange(returned, temp)) {
                  break;
               }

               this.bodyContainingNode = null;
            }
         }

         boolean changed = this.change(returned, temp);
         if (changed) {
            this.modified = true;
         }
      }
   }
}
