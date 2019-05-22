package corg.vfix.pg.jimple.init;

import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.vfg.VFGNode;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.Stmt;

public class RuleInitOne {
   private static SootMethod constructor = null;

   public static void patch(VFGNode node) throws Exception {
      Body body = (Body)node.getBody().clone();
      Type type = node.getETwo().getType();
      Stmt stmt = OperationLib.locateStmt(body, node.getStmt());
      Value value = OperationLib.locateValue(stmt, node.getETwo());
      if (stmt == null) {
         throw new Exception();
      } else if (value == null) {
         throw new Exception();
      } else {
         if (type instanceof ArrayType) {
            OperationLib.insertArrayInitBefore(body.getUnits(), value, stmt, (ArrayType)type);
         } else if (type instanceof RefType) {
            OperationLib.insertInitBefore(body.getUnits(), value, stmt, constructor);
         }

         OperationLib.output(node, body, "INIT-ONE");
         node.setNewClsName(constructor);
         constructor = null;
      }
   }

   private static boolean isApplicable(VFGNode node) {
      int nodeType = node.getNodeType();
      int stmtType = node.getStmtType();
      return stmtType == 0 && nodeType == 1 || stmtType == 1 && nodeType == 1 || stmtType == 1 && nodeType == 2 || stmtType == 2 && nodeType == 1 || stmtType == 2 && nodeType == 2 || stmtType == 3 && nodeType == 1 || stmtType == 3 && nodeType == 2 || stmtType == 7 && nodeType == 2 || stmtType == 7 && nodeType == 1 || stmtType == 8 && nodeType == 1 || stmtType == 9 && nodeType == 1;
   }

   private static boolean isPracticable(VFGNode node) {
      Value eTwo = node.getETwo();
      if (eTwo == null) {
         return false;
      } else if (!(eTwo instanceof Local)) {
         return false;
      } else {
         Type type = eTwo.getType();
         if (type instanceof ArrayType) {
            return true;
         } else if (type instanceof RefType) {
            if ("java.lang.Object".equals(type.toString())) {
               return false;
            } else {
               constructor = OperationLib.getImplementedConstructor(type);
               return constructor != null;
            }
         } else {
            return false;
         }
      }
   }

   public static boolean solve(VFGNode node) {
      return isApplicable(node) & isPracticable(node);
   }
}
