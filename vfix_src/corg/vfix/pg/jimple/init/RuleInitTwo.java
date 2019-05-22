package corg.vfix.pg.jimple.init;

import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.vfg.VFGNode;
import soot.ArrayType;
import soot.Body;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.Stmt;

public class RuleInitTwo {
   private static SootMethod constructor = null;

   public static void patch(VFGNode node) throws Exception {
      Body body = (Body)node.getBody().clone();
      Stmt stmt = OperationLib.locateStmt(body, node.getStmt());
      if (stmt == null) {
         throw new Exception();
      } else {
         Type type = node.getEOne().getType();
         if (type instanceof ArrayType) {
            if (node.getStmtType() == 6) {
               OperationLib.replaceNullAssignWithNewArray(body, stmt, type);
            } else if (node.getStmtType() == 5) {
               OperationLib.replaceNullRetWithNewArray(body, stmt, type);
            }
         } else if (type instanceof RefType) {
            if (node.getStmtType() == 6) {
               OperationLib.replaceNullAssignWithNew(body, stmt, constructor);
            } else if (node.getStmtType() == 5) {
               OperationLib.replaceNullRetWithNew(body, stmt, constructor);
            }

            node.setNewClsName(constructor);
         }

         OperationLib.output(node, body, "INIT-TWO");
         constructor = null;
      }
   }

   private static boolean isApplicable(VFGNode node) {
      int nodeType = node.getNodeType();
      int stmtType = node.getStmtType();
      return stmtType == 6 && nodeType == 0 || stmtType == 5 && nodeType == 0;
   }

   private static boolean isPracticable(VFGNode node) {
      Value eOne = node.getEOne();
      if (eOne == null) {
         return false;
      } else {
         Type type = eOne.getType();
         if (type instanceof ArrayType) {
            return true;
         } else if (type instanceof RefType) {
            constructor = OperationLib.getImplementedConstructor(type);
            return constructor != null;
         } else {
            return false;
         }
      }
   }

   public static boolean solve(VFGNode node) {
      return isApplicable(node) & isPracticable(node);
   }
}
