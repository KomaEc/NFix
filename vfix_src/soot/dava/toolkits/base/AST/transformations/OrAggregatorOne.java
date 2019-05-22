package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;

public class OrAggregatorOne extends DepthFirstAdapter {
   public OrAggregatorOne() {
   }

   public OrAggregatorOne(boolean verbose) {
      super(verbose);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
   }

   public void outASTLabeledBlockNode(ASTLabeledBlockNode node) {
      String outerLabel = node.get_Label().toString();
      if (outerLabel != null) {
         String innerLabel = null;
         ASTLabeledBlockNode secondLabeledBlockNode = this.isLabelWithinLabel(node);
         if (secondLabeledBlockNode != null) {
            innerLabel = secondLabeledBlockNode.get_Label().toString();
            if (innerLabel != null) {
               List secondLabelsBodies = this.getSecondLabeledBlockBodies(secondLabeledBlockNode);
               boolean allIfs = this.checkAllAreIfsWithProperBreaks(secondLabelsBodies.iterator(), outerLabel, innerLabel);
               if (allIfs) {
                  List<ASTCondition> conditions = this.getConditions(secondLabelsBodies.iterator());
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

                  List<Object> newIfBody = new ArrayList();
                  List<Object> subBodies = node.get_SubBodies();
                  List labeledBlockBody = (List)subBodies.get(0);
                  Iterator subBodiesIt = labeledBlockBody.iterator();
                  subBodiesIt.next();

                  while(subBodiesIt.hasNext()) {
                     ASTNode temp = (ASTNode)subBodiesIt.next();
                     newIfBody.add(temp);
                  }

                  ASTIfNode newNode = new ASTIfNode(new SETNodeLabel(), (ASTCondition)newCond, newIfBody);
                  List<Object> newLabeledBlockBody = new ArrayList();
                  newLabeledBlockBody.add(newNode);
                  G.v().ASTTransformations_modified = true;
                  node.replaceBody(newLabeledBlockBody);
                  UselessLabelFinder.v().findAndKill(node);
               }
            }
         }
      }
   }

   private ASTLabeledBlockNode isLabelWithinLabel(ASTLabeledBlockNode node) {
      List<Object> subBodies = node.get_SubBodies();
      if (subBodies.size() == 0) {
         node.set_Label(new SETNodeLabel());
         return null;
      } else {
         List bodies = (List)subBodies.get(0);
         if (bodies.size() == 0) {
            node.set_Label(new SETNodeLabel());
            return null;
         } else {
            ASTNode firstBody = (ASTNode)bodies.get(0);
            return !(firstBody instanceof ASTLabeledBlockNode) ? null : (ASTLabeledBlockNode)firstBody;
         }
      }
   }

   private List getSecondLabeledBlockBodies(ASTLabeledBlockNode secondLabeledBlockNode) {
      List<Object> secondLabelsSubBodies = secondLabeledBlockNode.get_SubBodies();
      if (secondLabelsSubBodies.size() == 0) {
         secondLabeledBlockNode.set_Label(new SETNodeLabel());
         return null;
      } else {
         List secondLabelsBodies = (List)secondLabelsSubBodies.get(0);
         return secondLabelsBodies;
      }
   }

   private boolean checkAllAreIfsWithProperBreaks(Iterator it, String outerLabel, String innerLabel) {
      while(true) {
         if (it.hasNext()) {
            ASTNode secondLabelsBody = (ASTNode)it.next();
            Stmt stmt = this.isIfNodeWithOneStatement(secondLabelsBody);
            if (stmt == null) {
               return false;
            }

            String labelBroken = this.breaksLabel(stmt);
            if (labelBroken == null) {
               return false;
            }

            if (labelBroken.compareTo(innerLabel) == 0 && it.hasNext() || labelBroken.compareTo(outerLabel) == 0 && !it.hasNext()) {
               continue;
            }

            return false;
         }

         return true;
      }
   }

   private String breaksLabel(Stmt stmt) {
      if (!(stmt instanceof DAbruptStmt)) {
         return null;
      } else {
         DAbruptStmt abStmt = (DAbruptStmt)stmt;
         if (!abStmt.is_Break()) {
            return null;
         } else {
            SETNodeLabel label = abStmt.getLabel();
            return label.toString();
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
}
