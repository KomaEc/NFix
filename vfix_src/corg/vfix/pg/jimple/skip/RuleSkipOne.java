package corg.vfix.pg.jimple.skip;

import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.analysis.DependencyAnalyzor;
import corg.vfix.sa.vfg.VFGNode;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;

public class RuleSkipOne {
   private static Stmt targetStmt;

   public static void patch(VFGNode node) throws Exception {
      Body body = (Body)node.getBody().clone();
      Stmt stmt = OperationLib.locateStmt(body, node.getStmt());
      Stmt tStmt = OperationLib.locateStmt(body, targetStmt);
      Value value = OperationLib.locateValue(stmt, node.getETwo());
      if (stmt != null && tStmt != null) {
         if (value == null) {
            throw new Exception();
         } else {
            OperationLib.insertNullCheckSkipTo(body.getUnits(), value, stmt, tStmt);
            OperationLib.output(node, body, "SKIP-ONE");
         }
      } else {
         throw new Exception();
      }
   }

   private static boolean isApplicable(VFGNode node) {
      if (node.getNodeType() == 0) {
         return false;
      } else if (node.getStmt() instanceof ReturnStmt) {
         return false;
      } else {
         return node.getETwo() != null;
      }
   }

   private static boolean isPracticable(VFGNode node) {
      if (!(node.getETwo() instanceof Local)) {
         return false;
      } else {
         Stmt skippedStmt = node.getStmt();
         List<Unit> invalid = DependencyAnalyzor.collectInvalidStmts(node.getBody(), skippedStmt);
         targetStmt = (Stmt)DependencyAnalyzor.findTargetStmt(node.getBody(), invalid);
         return invalid.contains(skippedStmt) && invalid.size() == 1;
      }
   }

   public static boolean solve(VFGNode node) {
      return isApplicable(node) & isPracticable(node);
   }
}
