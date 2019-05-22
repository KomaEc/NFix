package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;

public class StrengthenByIf {
   public static List<ASTNode> getNewNode(ASTNode loopNode, ASTIfNode ifNode) {
      List<Object> ifBody = ifNode.getIfBody();
      String label = isItOnlyBreak(ifBody);
      if (label != null) {
         if (((ASTLabeledNode)loopNode).get_Label().toString() != null && ((ASTLabeledNode)loopNode).get_Label().toString().compareTo(label) == 0) {
            ASTCondition innerCond;
            ArrayList toReturn;
            if (loopNode instanceof ASTWhileNode) {
               innerCond = ((ASTWhileNode)loopNode).get_Condition();
               ASTCondition innerCond = ifNode.get_Condition();
               innerCond.flip();
               ASTCondition newCond = new ASTAndCondition(innerCond, innerCond);
               toReturn = new ArrayList();
               SETNodeLabel newLabel = new SETNodeLabel();
               List<ASTNode> toReturn = new ArrayList();
               toReturn.add(new ASTWhileNode(newLabel, newCond, toReturn));
               return toReturn;
            }

            if (loopNode instanceof ASTDoWhileNode) {
               return null;
            }

            if (loopNode instanceof ASTUnconditionalLoopNode) {
               innerCond = ifNode.get_Condition();
               innerCond.flip();
               List<Object> newWhileBody = new ArrayList();
               SETNodeLabel newLabel = new SETNodeLabel();
               toReturn = new ArrayList();
               toReturn.add(new ASTWhileNode(newLabel, innerCond, newWhileBody));
               return toReturn;
            }
         }
      } else if (loopNode instanceof ASTUnconditionalLoopNode && ifBody.size() == 1) {
         ASTNode tempNode = (ASTNode)ifBody.get(0);
         if (tempNode instanceof ASTStatementSequenceNode) {
            List<AugmentedStmt> statements = ((ASTStatementSequenceNode)tempNode).getStatements();
            Iterator stIt = statements.iterator();

            while(true) {
               while(stIt.hasNext()) {
                  AugmentedStmt as = (AugmentedStmt)stIt.next();
                  Stmt stmt = as.get_Stmt();
                  if (stmt instanceof DAbruptStmt && !stIt.hasNext()) {
                     DAbruptStmt abStmt = (DAbruptStmt)stmt;
                     if (abStmt.is_Break()) {
                        String loopLabel = ((ASTLabeledNode)loopNode).get_Label().toString();
                        String breakLabel = abStmt.getLabel().toString();
                        if (loopLabel != null && breakLabel != null && loopLabel.compareTo(breakLabel) == 0) {
                           ASTCondition innerCond = ifNode.get_Condition();
                           innerCond.flip();
                           List<Object> newWhileBody = new ArrayList();
                           SETNodeLabel newLabel = ((ASTUnconditionalLoopNode)loopNode).get_Label();
                           List<ASTNode> toReturn = new ArrayList();
                           toReturn.add(new ASTWhileNode(newLabel, innerCond, newWhileBody));
                           Iterator<AugmentedStmt> tempIt = statements.iterator();
                           ArrayList newStmts = new ArrayList();

                           while(tempIt.hasNext()) {
                              AugmentedStmt tempStmt = (AugmentedStmt)tempIt.next();
                              if (tempIt.hasNext()) {
                                 newStmts.add(tempStmt);
                              }
                           }

                           toReturn.add(new ASTStatementSequenceNode(newStmts));
                           return toReturn;
                        }
                     }
                  } else if ((stmt instanceof ReturnStmt || stmt instanceof ReturnVoidStmt) && !stIt.hasNext()) {
                     ASTCondition innerCond = ifNode.get_Condition();
                     innerCond.flip();
                     List<Object> newWhileBody = new ArrayList();
                     SETNodeLabel newLabel = new SETNodeLabel();
                     List<ASTNode> toReturn = new ArrayList();
                     toReturn.add(new ASTWhileNode(newLabel, innerCond, newWhileBody));
                     Iterator<AugmentedStmt> tempIt = statements.iterator();
                     ArrayList newStmts = new ArrayList();

                     while(tempIt.hasNext()) {
                        newStmts.add(tempIt.next());
                     }

                     toReturn.add(new ASTStatementSequenceNode(newStmts));
                     return toReturn;
                  }
               }

               return null;
            }
         }
      }

      return null;
   }

   private static String isItOnlyBreak(List<Object> body) {
      if (body.size() != 1) {
         return null;
      } else {
         ASTNode tempNode = (ASTNode)body.get(0);
         if (!(tempNode instanceof ASTStatementSequenceNode)) {
            return null;
         } else {
            List<AugmentedStmt> statements = ((ASTStatementSequenceNode)tempNode).getStatements();
            if (statements.size() != 1) {
               return null;
            } else {
               AugmentedStmt as = (AugmentedStmt)statements.get(0);
               Stmt stmt = as.get_Stmt();
               if (!(stmt instanceof DAbruptStmt)) {
                  return null;
               } else {
                  DAbruptStmt abStmt = (DAbruptStmt)stmt;
                  return !abStmt.is_Break() ? null : abStmt.getLabel().toString();
               }
            }
         }
      }
   }
}
