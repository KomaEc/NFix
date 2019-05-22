package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;

public class PushLabeledBlockIn extends DepthFirstAdapter {
   public PushLabeledBlockIn() {
   }

   public PushLabeledBlockIn(boolean verbose) {
      super(verbose);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
   }

   public void outASTLabeledBlockNode(ASTLabeledBlockNode node) {
      String label = node.get_Label().toString();
      List<Object> subBodies = node.get_SubBodies();
      if (subBodies.size() == 1) {
         List subBody = (List)subBodies.get(0);
         int nodeNumber = this.checkForBreak(subBody, label);
         if (nodeNumber > -1) {
            if (subBody.size() < nodeNumber) {
               throw new RuntimeException("Please submit this benchmark as a bug");
            }

            if (nodeNumber + 1 != subBody.size()) {
               return;
            }

            ASTNode temp = (ASTNode)subBody.get(nodeNumber);
            if (!(temp instanceof ASTLabeledNode)) {
               return;
            }

            ASTLabeledNode tempNode = (ASTLabeledNode)temp;
            String innerLabel = tempNode.get_Label().toString();
            if (innerLabel != null) {
               if (subBody.size() == 1) {
                  boolean done = this.replaceBreakLabels(temp, label, innerLabel);
                  if (done) {
                     node.set_Label(new SETNodeLabel());
                     G.v().ASTTransformations_modified = true;
                  }
               }

               return;
            }

            SETNodeLabel newLabel = new SETNodeLabel();
            newLabel.set_Name(label);
            tempNode.set_Label(newLabel);
            node.set_Label(new SETNodeLabel());
            G.v().ASTTransformations_modified = true;
         }

      }
   }

   private boolean replaceBreakLabels(ASTNode node, String toReplace, String replaceWith) {
      boolean toReturn = false;
      List<Object> subBodies = node.get_SubBodies();
      Iterator subIt = subBodies.iterator();

      label46:
      while(subIt.hasNext()) {
         List subBody = null;
         if (node instanceof ASTTryNode) {
            ASTTryNode.container subBodyContainer = (ASTTryNode.container)subIt.next();
            subBody = (List)subBodyContainer.o;
         } else {
            subBody = (List)subIt.next();
         }

         Iterator it = subBody.iterator();

         while(true) {
            while(true) {
               if (!it.hasNext()) {
                  continue label46;
               }

               ASTNode temp = (ASTNode)it.next();
               if (temp instanceof ASTStatementSequenceNode) {
                  ASTStatementSequenceNode stmtSeq = (ASTStatementSequenceNode)temp;
                  Iterator var11 = stmtSeq.getStatements().iterator();

                  while(var11.hasNext()) {
                     AugmentedStmt as = (AugmentedStmt)var11.next();
                     Stmt s = as.get_Stmt();
                     String labelBroken = this.isAbrupt(s);
                     if (labelBroken != null && labelBroken.compareTo(toReplace) == 0) {
                        this.replaceLabel(s, replaceWith);
                        toReturn = true;
                     }
                  }
               } else {
                  boolean returnVal = this.replaceBreakLabels(temp, toReplace, replaceWith);
                  if (returnVal) {
                     toReturn = true;
                  }
               }
            }
         }
      }

      return toReturn;
   }

   private int checkForBreak(List ASTNodeBody, String outerLabel) {
      Iterator it = ASTNodeBody.iterator();

      for(int nodeNumber = 0; it.hasNext(); ++nodeNumber) {
         ASTNode temp = (ASTNode)it.next();
         Iterator subIt;
         if (temp instanceof ASTStatementSequenceNode) {
            ASTStatementSequenceNode stmtSeq = (ASTStatementSequenceNode)temp;
            subIt = stmtSeq.getStatements().iterator();

            while(subIt.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)subIt.next();
               Stmt s = as.get_Stmt();
               String labelBroken = this.breaksLabel(s);
               if (labelBroken != null && outerLabel != null && labelBroken.compareTo(outerLabel) == 0) {
                  return nodeNumber;
               }
            }
         } else {
            List<Object> subBodies = temp.get_SubBodies();
            subIt = subBodies.iterator();

            while(subIt.hasNext()) {
               if (temp instanceof ASTTryNode) {
                  ASTTryNode.container subBody = (ASTTryNode.container)subIt.next();
                  if (this.checkForBreak((List)subBody.o, outerLabel) > -1) {
                     return nodeNumber;
                  }
               } else if (this.checkForBreak((List)subIt.next(), outerLabel) > -1) {
                  return nodeNumber;
               }
            }
         }
      }

      return -1;
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

   private String isAbrupt(Stmt stmt) {
      if (!(stmt instanceof DAbruptStmt)) {
         return null;
      } else {
         DAbruptStmt abStmt = (DAbruptStmt)stmt;
         if (!abStmt.is_Break() && !abStmt.is_Continue()) {
            return null;
         } else {
            SETNodeLabel label = abStmt.getLabel();
            return label.toString();
         }
      }
   }

   private void replaceLabel(Stmt s, String replaceWith) {
      DAbruptStmt abStmt = (DAbruptStmt)s;
      SETNodeLabel label = abStmt.getLabel();
      label.set_Name(replaceWith);
   }
}
