package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.Stmt;

public class AndAggregator extends DepthFirstAdapter {
   public AndAggregator() {
   }

   public AndAggregator(boolean verbose) {
      super(verbose);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
   }

   public void outASTIfNode(ASTIfNode node) {
      List<Object> bodies = node.get_SubBodies();
      if (bodies.size() == 1) {
         List body = (List)bodies.get(0);
         if (body.size() == 1) {
            ASTNode bodyNode = (ASTNode)body.get(0);
            if (bodyNode instanceof ASTIfNode) {
               ASTCondition outerCond = node.get_Condition();
               ASTCondition innerCond = ((ASTIfNode)bodyNode).get_Condition();
               SETNodeLabel outerLabel = node.get_Label();
               SETNodeLabel innerLabel = ((ASTIfNode)bodyNode).get_Label();
               SETNodeLabel newLabel = null;
               if (outerLabel.toString() == null && innerLabel.toString() == null) {
                  newLabel = outerLabel;
               } else if (outerLabel.toString() != null && innerLabel.toString() == null) {
                  newLabel = outerLabel;
               } else if (outerLabel.toString() == null && innerLabel.toString() != null) {
                  newLabel = innerLabel;
               } else if (outerLabel.toString() != null && innerLabel.toString() != null) {
                  newLabel = outerLabel;
                  this.changeUses(outerLabel.toString(), innerLabel.toString(), bodyNode);
               }

               ASTCondition newCond = new ASTAndCondition(outerCond, innerCond);
               List<Object> newBodyList = ((ASTIfNode)bodyNode).get_SubBodies();
               if (newBodyList.size() == 1) {
                  List<Object> newBody = (List)newBodyList.get(0);
                  node.replace(newLabel, newCond, newBody);
                  G.v().ASTTransformations_modified = true;
               }
            }
         }
      }

   }

   private void changeUses(String to, String from, ASTNode node) {
      List<Object> subBodies = node.get_SubBodies();
      Iterator it = subBodies.iterator();

      while(true) {
         label38:
         while(it.hasNext()) {
            Iterator nodesIt;
            if (node instanceof ASTStatementSequenceNode) {
               ASTStatementSequenceNode stmtSeq = (ASTStatementSequenceNode)node;
               nodesIt = stmtSeq.getStatements().iterator();

               while(true) {
                  DAbruptStmt abStmt;
                  do {
                     Stmt s;
                     do {
                        if (!nodesIt.hasNext()) {
                           continue label38;
                        }

                        AugmentedStmt as = (AugmentedStmt)nodesIt.next();
                        s = as.get_Stmt();
                     } while(!(s instanceof DAbruptStmt));

                     abStmt = (DAbruptStmt)s;
                  } while(!abStmt.is_Break() && !abStmt.is_Continue());

                  SETNodeLabel label = abStmt.getLabel();
                  String labelBroken = label.toString();
                  if (labelBroken != null && labelBroken.compareTo(from) == 0) {
                     label.set_Name(to);
                  }
               }
            } else {
               List subBodyNodes = null;
               if (node instanceof ASTTryNode) {
                  ASTTryNode.container subBody = (ASTTryNode.container)it.next();
                  subBodyNodes = (List)subBody.o;
               } else {
                  subBodyNodes = (List)it.next();
               }

               nodesIt = subBodyNodes.iterator();

               while(nodesIt.hasNext()) {
                  this.changeUses(to, from, (ASTNode)nodesIt.next());
               }
            }
         }

         return;
      }
   }
}
