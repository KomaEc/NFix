package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Singletons;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.Stmt;

public class UselessLabelFinder {
   public static boolean DEBUG = false;

   public UselessLabelFinder(Singletons.Global g) {
   }

   public static UselessLabelFinder v() {
      return G.v().soot_dava_toolkits_base_AST_transformations_UselessLabelFinder();
   }

   public boolean findAndKill(ASTNode node) {
      if (!(node instanceof ASTLabeledNode)) {
         if (DEBUG) {
            System.out.println("Returning from findAndKill for node of type " + node.getClass());
         }

         return false;
      } else {
         if (DEBUG) {
            System.out.println("FindAndKill continuing for node fo type" + node.getClass());
         }

         String label = ((ASTLabeledNode)node).get_Label().toString();
         if (label == null) {
            return false;
         } else {
            if (DEBUG) {
               System.out.println("dealing with labeled node" + label);
            }

            List<Object> subBodies = node.get_SubBodies();
            Iterator it = subBodies.iterator();

            List subBodyTemp;
            do {
               if (!it.hasNext()) {
                  ((ASTLabeledNode)node).set_Label(new SETNodeLabel());
                  if (DEBUG) {
                     System.out.println("USELESS LABEL DETECTED");
                  }

                  return true;
               }

               subBodyTemp = null;
               if (node instanceof ASTTryNode) {
                  ASTTryNode.container subBody = (ASTTryNode.container)it.next();
                  subBodyTemp = (List)subBody.o;
               } else {
                  subBodyTemp = (List)it.next();
               }
            } while(!this.checkForBreak(subBodyTemp, label));

            return false;
         }
      }
   }

   private boolean checkForBreak(List ASTNodeBody, String outerLabel) {
      Iterator it = ASTNodeBody.iterator();

      while(true) {
         while(it.hasNext()) {
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
                     return true;
                  }
               }
            } else {
               List<Object> subBodies = temp.get_SubBodies();
               subIt = subBodies.iterator();

               while(subIt.hasNext()) {
                  List subBodyTemp = null;
                  if (temp instanceof ASTTryNode) {
                     ASTTryNode.container subBody = (ASTTryNode.container)subIt.next();
                     subBodyTemp = (List)subBody.o;
                  } else {
                     subBodyTemp = (List)subIt.next();
                  }

                  if (this.checkForBreak(subBodyTemp, outerLabel)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private String breaksLabel(Stmt stmt) {
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
}
