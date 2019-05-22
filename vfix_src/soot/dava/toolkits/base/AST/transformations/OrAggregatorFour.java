package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;

public class OrAggregatorFour extends DepthFirstAdapter {
   public OrAggregatorFour() {
   }

   public OrAggregatorFour(boolean verbose) {
      super(verbose);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
   }

   public void outASTForLoopNode(ASTForLoopNode node) {
      String label = node.get_Label().toString();
      if (label != null) {
         List<Object> subBodies = node.get_SubBodies();
         List<Object> newBody = this.matchPattern(label, subBodies);
         if (newBody != null) {
            node.replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         }

         UselessLabelFinder.v().findAndKill(node);
      }
   }

   public void outASTWhileNode(ASTWhileNode node) {
      String label = node.get_Label().toString();
      if (label != null) {
         List<Object> subBodies = node.get_SubBodies();
         List<Object> newBody = this.matchPattern(label, subBodies);
         if (newBody != null) {
            node.replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         }

         UselessLabelFinder.v().findAndKill(node);
      }
   }

   public void outASTDoWhileNode(ASTDoWhileNode node) {
      String label = node.get_Label().toString();
      if (label != null) {
         List<Object> subBodies = node.get_SubBodies();
         List<Object> newBody = this.matchPattern(label, subBodies);
         if (newBody != null) {
            node.replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         }

         UselessLabelFinder.v().findAndKill(node);
      }
   }

   public void outASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      String label = node.get_Label().toString();
      if (label != null) {
         List<Object> subBodies = node.get_SubBodies();
         List<Object> newBody = this.matchPattern(label, subBodies);
         if (newBody != null) {
            node.replaceBody(newBody);
            G.v().ASTTransformations_modified = true;
         }

         UselessLabelFinder.v().findAndKill(node);
      }
   }

   public List<Object> matchPattern(String whileLabel, List<Object> subBodies) {
      if (subBodies.size() != 1) {
         return null;
      } else {
         List subBody = (List)subBodies.get(0);
         Iterator it = subBody.iterator();
         int nodeNumber = 0;

         while(true) {
            while(it.hasNext()) {
               ASTNode temp = (ASTNode)it.next();
               if (temp instanceof ASTLabeledBlockNode) {
                  ASTLabeledBlockNode labeledNode = (ASTLabeledBlockNode)temp;
                  String innerLabel = labeledNode.get_Label().toString();
                  if (innerLabel == null) {
                     ++nodeNumber;
                     continue;
                  }

                  List<Object> labeledBlocksSubBodies = labeledNode.get_SubBodies();
                  if (labeledBlocksSubBodies.size() != 1) {
                     ++nodeNumber;
                     continue;
                  }

                  List labeledBlocksSubBody = (List)labeledBlocksSubBodies.get(0);
                  boolean allIfs = this.checkAllAreIfsWithProperBreaks(labeledBlocksSubBody.iterator(), whileLabel, innerLabel);
                  if (!allIfs) {
                     ++nodeNumber;
                     continue;
                  }

                  List<Object> whileBody = this.createWhileBody(subBody, labeledBlocksSubBody, nodeNumber);
                  if (whileBody != null) {
                     return whileBody;
                  }
               }

               ++nodeNumber;
            }

            return null;
         }
      }
   }

   private List<Object> createWhileBody(List subBody, List labeledBlocksSubBody, int nodeNumber) {
      List<Object> bodyA = new ArrayList();
      Iterator it = subBody.iterator();

      for(int index = 0; index != nodeNumber; ++index) {
         if (!it.hasNext()) {
            return null;
         }

         bodyA.add(it.next());
      }

      List<ASTCondition> conditions = this.getConditions(labeledBlocksSubBody.iterator());
      Iterator<ASTCondition> condIt = conditions.iterator();
      Object newCond = null;

      while(condIt.hasNext()) {
         ASTCondition next = (ASTCondition)condIt.next();
         if (newCond == null) {
            newCond = next;
         } else {
            newCond = new ASTOrCondition((ASTCondition)newCond, next);
         }
      }

      it.next();
      ArrayList bodyB = new ArrayList();

      while(it.hasNext()) {
         bodyB.add(it.next());
      }

      ASTIfNode newNode = new ASTIfNode(new SETNodeLabel(), (ASTCondition)newCond, bodyB);
      bodyA.add(newNode);
      return bodyA;
   }

   private List<ASTCondition> getConditions(Iterator it) {
      ArrayList toReturn = new ArrayList();

      while(it.hasNext()) {
         ASTIfNode node = (ASTIfNode)it.next();
         ASTCondition cond = node.get_Condition();
         if (it.hasNext()) {
            toReturn.add(cond);
         } else {
            cond.flip();
            toReturn.add(cond);
         }
      }

      return toReturn;
   }

   private boolean checkAllAreIfsWithProperBreaks(Iterator it, String outerLabel, String innerLabel) {
      while(true) {
         if (it.hasNext()) {
            ASTNode secondLabelsBody = (ASTNode)it.next();
            Stmt stmt = this.isIfNodeWithOneStatement(secondLabelsBody);
            if (stmt == null) {
               return false;
            }

            boolean abrupt = this.abruptLabel(stmt, outerLabel, innerLabel, it.hasNext());
            if (abrupt) {
               continue;
            }

            return false;
         }

         return true;
      }
   }

   private boolean abruptLabel(Stmt stmt, String outerLabel, String innerLabel, boolean hasNext) {
      if (!(stmt instanceof DAbruptStmt)) {
         return false;
      } else {
         DAbruptStmt abStmt = (DAbruptStmt)stmt;
         SETNodeLabel label = abStmt.getLabel();
         String abruptLabel = label.toString();
         if (abruptLabel == null) {
            return false;
         } else if (abStmt.is_Break() && abruptLabel.compareTo(innerLabel) == 0 && hasNext) {
            return true;
         } else {
            return abStmt.is_Continue() && abruptLabel.compareTo(outerLabel) == 0 && !hasNext;
         }
      }
   }

   private Stmt isIfNodeWithOneStatement(ASTNode secondLabelsBody) {
      if (!(secondLabelsBody instanceof ASTIfNode)) {
         return null;
      } else {
         ASTIfNode ifNode = (ASTIfNode)secondLabelsBody;
         List<Object> ifSubBodies = ifNode.get_SubBodies();
         if (ifSubBodies.size() != 1) {
            return null;
         } else {
            List ifBody = (List)ifSubBodies.get(0);
            if (ifBody.size() != 1) {
               return null;
            } else {
               ASTNode ifBodysBody = (ASTNode)ifBody.get(0);
               if (!(ifBodysBody instanceof ASTStatementSequenceNode)) {
                  return null;
               } else {
                  List<AugmentedStmt> statements = ((ASTStatementSequenceNode)ifBodysBody).getStatements();
                  if (statements.size() != 1) {
                     return null;
                  } else {
                     AugmentedStmt as = (AugmentedStmt)statements.get(0);
                     Stmt s = as.get_Stmt();
                     return s;
                  }
               }
            }
         }
      }
   }
}
