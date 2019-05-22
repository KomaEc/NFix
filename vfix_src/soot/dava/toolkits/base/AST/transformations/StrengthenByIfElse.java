package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfElseNode;
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

public class StrengthenByIfElse {
   public static List<ASTNode> getNewNode(ASTNode loopNode, ASTIfElseNode ifElseNode) {
      List<Object> elseBody = ifElseNode.getElseBody();
      if (elseBody.size() != 1) {
         return null;
      } else {
         ASTNode tempNode = (ASTNode)elseBody.get(0);
         if (!(tempNode instanceof ASTStatementSequenceNode)) {
            return null;
         } else {
            List<AugmentedStmt> statements = ((ASTStatementSequenceNode)tempNode).getStatements();
            Iterator stmtIt = statements.iterator();

            Stmt stmt;
            label85:
            do {
               String labelBroken;
               String loopLabel;
               do {
                  if (!stmtIt.hasNext()) {
                     return null;
                  }

                  AugmentedStmt as = (AugmentedStmt)stmtIt.next();
                  stmt = as.get_Stmt();
                  if (!(stmt instanceof DAbruptStmt)) {
                     continue label85;
                  }

                  DAbruptStmt abStmt = (DAbruptStmt)stmt;
                  if (!abStmt.is_Break()) {
                     return null;
                  }

                  if (stmtIt.hasNext()) {
                     return null;
                  }

                  SETNodeLabel label = abStmt.getLabel();
                  labelBroken = label.toString();
                  loopLabel = ((ASTLabeledNode)loopNode).get_Label().toString();
               } while(labelBroken == null || loopLabel == null || labelBroken.compareTo(loopLabel) != 0);

               if (loopNode instanceof ASTWhileNode && statements.size() != 1) {
                  return null;
               }

               ASTWhileNode newWhileNode = makeWhileNode(ifElseNode, loopNode);
               if (newWhileNode == null) {
                  return null;
               }

               List<ASTNode> toReturn = new ArrayList();
               toReturn.add(newWhileNode);
               if (statements.size() != 1) {
                  Iterator<AugmentedStmt> tempIt = statements.iterator();
                  ArrayList newStmts = new ArrayList();

                  while(tempIt.hasNext()) {
                     AugmentedStmt tempStmt = (AugmentedStmt)tempIt.next();
                     if (tempIt.hasNext()) {
                        newStmts.add(tempStmt);
                     }
                  }

                  toReturn.add(new ASTStatementSequenceNode(newStmts));
               }

               return toReturn;
            } while(!(stmt instanceof ReturnStmt) && !(stmt instanceof ReturnVoidStmt));

            if (!(loopNode instanceof ASTUnconditionalLoopNode)) {
               return null;
            } else if (stmtIt.hasNext()) {
               return null;
            } else {
               ASTWhileNode newWhileNode = makeWhileNode(ifElseNode, loopNode);
               if (newWhileNode == null) {
                  return null;
               } else {
                  List<ASTNode> toReturn = new ArrayList();
                  toReturn.add(newWhileNode);
                  List<AugmentedStmt> newStmts = new ArrayList(statements);
                  toReturn.add(new ASTStatementSequenceNode(newStmts));
                  return toReturn;
               }
            }
         }
      }
   }

   private static ASTWhileNode makeWhileNode(ASTIfElseNode ifElseNode, ASTNode loopNode) {
      ASTCondition outerCond = null;
      ASTCondition innerCond = ifElseNode.get_Condition();
      ASTCondition newCond = null;
      if (loopNode instanceof ASTWhileNode) {
         outerCond = ((ASTWhileNode)loopNode).get_Condition();
         newCond = new ASTAndCondition(outerCond, innerCond);
      } else {
         if (!(loopNode instanceof ASTUnconditionalLoopNode)) {
            return null;
         }

         newCond = innerCond;
      }

      List<Object> loopBody = ifElseNode.getIfBody();
      SETNodeLabel newLabel = ((ASTLabeledNode)loopNode).get_Label();
      return new ASTWhileNode(newLabel, (ASTCondition)newCond, loopBody);
   }
}
