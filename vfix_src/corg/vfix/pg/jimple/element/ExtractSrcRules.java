package corg.vfix.pg.jimple.element;

import corg.vfix.sa.vfg.VFGNode;
import soot.Value;
import soot.jimple.AssignStmt;

public class ExtractSrcRules {
   public static boolean main(VFGNode node) throws Exception {
      if (node.getStmtType() == 3) {
         extractSrcCall(node);
      } else if (node.getStmtType() == 6) {
         extractSrcNullAssign(node);
      } else if (node.getStmtType() == 5) {
         extractSrcNullRet(node);
      } else {
         if (node.getStmtType() != 1) {
            if (node.getStmtType() == 0) {
               System.out.println("Cannot extract elements from Src Copy ");
               return false;
            }

            System.out.println("Cannot extract elements: " + node.getStmt());
            System.out.println(StmtType.typeToString(node.getStmtType()));
            return false;
         }

         extractSrcLoad(node);
      }

      return true;
   }

   private static void extractSrcLoad(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne(stmt.getLeftOp());
      node.setETwo(stmt.getRightOp());
   }

   private static void extractSrcNullRet(VFGNode node) {
      node.setEOne((Value)null);
      node.setETwo((Value)null);
   }

   private static void extractSrcNullAssign(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne(stmt.getLeftOp());
      node.setETwo((Value)null);
   }

   private static void extractSrcCall(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne(stmt.getLeftOp());
      node.setETwo(stmt.getRightOp());
   }
}
