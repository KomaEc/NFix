package corg.vfix.pg.jimple.element;

import corg.vfix.sa.vfg.VFGNode;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;

public class ExtractSinkRules {
   public static boolean main(VFGNode node) throws Exception {
      if (node.getStmtType() == 1) {
         extractSinkLoad(node);
      } else if (node.getStmtType() == 2) {
         extractSinkStore(node);
      } else if (node.getStmtType() == 3) {
         extractSinkInstanceCall(node);
      } else if (node.getStmtType() == 7) {
         extractSinkInstanceInvoke(node);
      } else if (node.getStmtType() == 6) {
         extractSinkNullAssign(node);
      } else {
         if (node.getStmtType() != 13) {
            System.out.println("Cannot extract elements: " + node.getStmt());
            System.out.println(StmtType.typeToString(node.getStmtType()));
            return false;
         }

         extractSinkLengthOf(node);
      }

      return true;
   }

   private static void extractSinkLengthOf(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      LengthExpr rightOp = (LengthExpr)stmt.getRightOp();
      node.setEOne((Value)null);
      node.setETwo(rightOp.getOp());
   }

   private static void extractSinkNullAssign(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne((Value)null);
      InstanceFieldRef leftOp = (InstanceFieldRef)stmt.getLeftOp();
      node.setEOne(leftOp.getBase());
      node.setETwo(stmt.getRightOp());
   }

   private static void extractSinkInstanceInvoke(VFGNode node) {
      InvokeStmt stmt = (InvokeStmt)node.getStmt();
      node.setEOne((Value)null);
      InstanceInvokeExpr rightOp = (InstanceInvokeExpr)stmt.getInvokeExpr();
      node.setETwo(rightOp.getBase());
   }

   private static void extractSinkInstanceCall(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne(stmt.getLeftOp());
      InstanceInvokeExpr rightOp = (InstanceInvokeExpr)stmt.getRightOp();
      node.setETwo(rightOp.getBase());
   }

   private static void extractSinkStore(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      InstanceFieldRef leftOp = (InstanceFieldRef)stmt.getLeftOp();
      node.setEOne(leftOp.getBase());
      node.setETwo(stmt.getRightOp());
   }

   private static void extractSinkLoad(VFGNode node) {
      AssignStmt stmt = (AssignStmt)node.getStmt();
      node.setEOne(stmt.getLeftOp());
      InstanceFieldRef rightOp = (InstanceFieldRef)stmt.getRightOp();
      node.setETwo(rightOp.getBase());
   }
}
