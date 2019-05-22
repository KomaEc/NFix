package corg.vfix.pg.jimple.init;

import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.analysis.DominateAnalyzor;
import corg.vfix.sa.analysis.JimpleLoopFinder;
import corg.vfix.sa.vfg.VFGNode;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.PrimType;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.Stmt;

public class RuleInitThree {
   private static SootMethod constructor = null;

   public static void patch(VFGNode node) throws Exception {
      Body body = (Body)node.getBody().clone();
      Stmt stmt = OperationLib.locateStmt(body, node.getStmt());
      Value eOne = OperationLib.locateValue(stmt, node.getEOne());
      Value eTwo = OperationLib.locateValue(stmt, node.getETwo());
      if (stmt == null) {
         throw new Exception();
      } else if (eOne != null && eTwo != null) {
         Type type = node.getEOne().getType();
         if (type instanceof ArrayType) {
            OperationLib.insertReplaceBefore(body.getUnits(), eOne, eTwo, stmt, (ArrayType)((ArrayType)type));
         } else if (type instanceof RefType) {
            OperationLib.insertReplaceBefore(body.getUnits(), eOne, eTwo, stmt, (SootMethod)constructor);
         } else if (type instanceof PrimType) {
            OperationLib.insertReplaceBefore(body.getUnits(), eOne, eTwo, stmt, (Value)OperationLib.getDefaultValue(type));
         }

         OperationLib.output(node, body, "INIT-THREE");
         node.setNewClsName(constructor);
         constructor = null;
      } else {
         throw new Exception();
      }
   }

   private static boolean isApplicable(VFGNode node) {
      int nodeType = node.getNodeType();
      int stmtType = node.getStmtType();
      return (stmtType == 1 && nodeType == 2 || stmtType == 3 && nodeType == 2) && !DominateAnalyzor.hasPostDomUse(node);
   }

   private static boolean isPracticable(VFGNode node) {
      Value eTwo = node.getETwo();
      if (eTwo == null) {
         return false;
      } else if ("java.lang.String".equals(eTwo.getType().toString())) {
         return false;
      } else if (isInLoop(node)) {
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

   private static boolean isInLoop(VFGNode node) {
      return JimpleLoopFinder.inLoop(node.getBody(), node.getStmt());
   }

   public static boolean solve(VFGNode node) {
      return isApplicable(node) & isPracticable(node);
   }
}
