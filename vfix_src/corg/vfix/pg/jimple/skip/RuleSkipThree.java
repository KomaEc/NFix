package corg.vfix.pg.jimple.skip;

import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.vfg.VFGNode;
import java.util.ArrayList;
import soot.Body;
import soot.Local;
import soot.Value;
import soot.jimple.Stmt;

public class RuleSkipThree {
   public static void patch(VFGNode node) throws Exception {
      Body body = (Body)node.getBody().clone();
      Stmt stmt = OperationLib.locateStmt(body, node.getStmt());
      Value value = OperationLib.locateValue(stmt, node.getETwo());
      if (stmt == null) {
         throw new Exception();
      } else if (value == null) {
         System.out.println("nodeStmt:" + node.getStmt());
         System.out.println("locatedStmt: " + stmt);
         System.out.println("value: " + node.getETwo());
         throw new Exception();
      } else {
         ArrayList<Stmt> newRetStmts = OperationLib.makeNewRetStmts(body, node.getMethod().getReturnType(), node);
         OperationLib.insertNullCheck(body.getUnits(), value, stmt, (ArrayList)newRetStmts);
         OperationLib.output(node, body, "SKIP-THREE");
      }
   }

   private static boolean isApplicable(VFGNode node) {
      if (node.getNodeType() == 0) {
         return false;
      } else {
         return node.getETwo() != null;
      }
   }

   private static boolean isPracticable(VFGNode node) {
      return node.getETwo() instanceof Local;
   }

   public static boolean solve(VFGNode node) {
      return isApplicable(node) & isPracticable(node);
   }
}
