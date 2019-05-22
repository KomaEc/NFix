package corg.vfix.pg.jimple.element;

import corg.vfix.sa.vfg.VFGNode;
import java.util.ArrayList;
import java.util.Iterator;
import soot.Local;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;

public class StmtType {
   public static final int COPY = 0;
   public static final int LOAD = 1;
   public static final int STORE = 2;
   public static final int INSTANCE_CALL = 3;
   public static final int RET = 4;
   public static final int NULL_RET = 5;
   public static final int NULL_ASSIGN = 6;
   public static final int INSTANCE_INVOKE = 7;
   public static final int CAST = 8;
   public static final int STATIC_CALL = 9;
   public static final int STATIC_INVOKE = 10;
   public static final int STATIC_PUT = 11;
   public static final int STATIC_GET = 12;
   public static final int LENGTH_OF = 13;
   public static final int UNTYPED = -1;

   private static int getStmtType(Stmt stmt) {
      if (isCopy(stmt)) {
         return 0;
      } else if (isLoad(stmt)) {
         return 1;
      } else if (isStore(stmt)) {
         return 2;
      } else if (isNULLAssign(stmt)) {
         return 6;
      } else if (isNULLRet(stmt)) {
         return 5;
      } else if (isInstanceCall(stmt)) {
         return 3;
      } else if (isInstanceInvoke(stmt)) {
         return 7;
      } else if (isRet(stmt)) {
         return 4;
      } else if (isCast(stmt)) {
         return 8;
      } else if (isStaticCall(stmt)) {
         return 9;
      } else if (isStaticInvoke(stmt)) {
         return 10;
      } else if (isStaticGet(stmt)) {
         return 12;
      } else if (isStaticPut(stmt)) {
         return 11;
      } else {
         return isLengthOf(stmt) ? 13 : -1;
      }
   }

   private static boolean isLengthOf(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         return isLocal(assign.getLeftOp()) && isLenthOfExpr(assign.getRightOp());
      } else {
         return false;
      }
   }

   private static boolean isLenthOfExpr(Value rightOp) {
      return rightOp instanceof LengthExpr;
   }

   public static boolean isStaticGet(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         return isLocal(assign.getLeftOp()) && isStaticFieldRef(assign.getRightOp());
      } else {
         return false;
      }
   }

   public static boolean isStaticPut(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         return isStaticFieldRef(assign.getLeftOp()) && isLocal(assign.getRightOp());
      } else {
         return false;
      }
   }

   private static boolean isStaticInvoke(Stmt stmt) {
      if (stmt instanceof InvokeStmt) {
         InvokeStmt invokeStmt = (InvokeStmt)stmt;
         return isStaticInvokeExpr(invokeStmt.getInvokeExpr());
      } else {
         return false;
      }
   }

   private static boolean isStaticCall(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         Value rightOp = assign.getRightOp();
         if (rightOp instanceof InvokeExpr) {
            InvokeExpr invokeExpr = (InvokeExpr)rightOp;
            if (isLocal(assign.getLeftOp()) && isStaticInvokeExpr(invokeExpr)) {
               return true;
            }

            return false;
         }
      }

      return false;
   }

   private static boolean isCast(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         Value rightOp = assign.getRightOp();
         if (rightOp instanceof CastExpr) {
            return true;
         }
      }

      return false;
   }

   public static boolean isRet(Stmt stmt) {
      if (stmt instanceof ReturnStmt) {
         ReturnStmt ret = (ReturnStmt)stmt;
         return !isConstant(ret.getOp());
      } else {
         return false;
      }
   }

   private static boolean isInstanceInvoke(Stmt stmt) {
      if (stmt instanceof InvokeStmt) {
         InvokeStmt invokeStmt = (InvokeStmt)stmt;
         return isInstanceInvokeExpr(invokeStmt.getInvokeExpr());
      } else {
         return false;
      }
   }

   private static boolean isInstanceInvokeExpr(InvokeExpr invokeExpr) {
      return invokeExpr instanceof InstanceInvokeExpr;
   }

   private static boolean isStaticInvokeExpr(InvokeExpr invokeExpr) {
      return invokeExpr instanceof StaticInvokeExpr;
   }

   private static boolean isStaticFieldRef(Value value) {
      return value instanceof StaticFieldRef;
   }

   private static boolean isInstanceCall(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         Value rightOp = assign.getRightOp();
         if (rightOp instanceof InvokeExpr) {
            InvokeExpr invokeExpr = (InvokeExpr)rightOp;
            if (isLocal(assign.getLeftOp()) && isInstanceInvokeExpr(invokeExpr)) {
               return true;
            }

            return false;
         }
      }

      return false;
   }

   public static boolean isCopy(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         return isLocalOrStatic(assign.getLeftOp()) && isLocalOrStatic(assign.getRightOp());
      } else {
         return false;
      }
   }

   private static boolean isNULLRet(Stmt stmt) {
      if (stmt instanceof ReturnStmt) {
         ReturnStmt ret = (ReturnStmt)stmt;
         return isNull(ret.getOp());
      } else {
         return false;
      }
   }

   public static boolean isNULLAssign(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         return isNull(assign.getRightOp());
      } else {
         return false;
      }
   }

   public static boolean isLoad(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         return isLocal(assign.getLeftOp()) && isInstanceFieldRef(assign.getRightOp());
      } else {
         return false;
      }
   }

   public static boolean isStore(Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         return isInstanceFieldRef(assign.getLeftOp()) && isLocal(assign.getRightOp());
      } else {
         return false;
      }
   }

   private static boolean isInstanceFieldRef(Value value) {
      return value instanceof InstanceFieldRef;
   }

   private static boolean isNull(Value value) {
      return value instanceof NullConstant;
   }

   private static boolean isLocalOrStatic(Value value) {
      return isLocal(value) || isStatic(value);
   }

   private static boolean isStatic(Value value) {
      return value instanceof StaticFieldRef;
   }

   private static boolean isLocal(Value value) {
      return value instanceof Local;
   }

   private static boolean isConstant(Value value) {
      return value instanceof Constant;
   }

   public static void setStmtType(VFGNode node) {
      node.setStmtType(getStmtType(node.getStmt()));
   }

   public static void setStmtsType(ArrayList<VFGNode> nodes) {
      Iterator var2 = nodes.iterator();

      while(var2.hasNext()) {
         VFGNode node = (VFGNode)var2.next();
         setStmtType(node);
      }

   }

   public static String typeToString(int type) {
      switch(type) {
      case 0:
         return "COPY";
      case 1:
         return "LOAD";
      case 2:
         return "STORE";
      case 3:
         return "INSTANCE_CALL";
      case 4:
         return "RET";
      case 5:
         return "NULL_RET";
      case 6:
         return "NULL_ASSIGN";
      case 7:
         return "INSTANCE_INVOKE";
      case 8:
         return "CAST";
      case 9:
         return "STATIC_CALL";
      case 10:
         return "STATIC_INVOKE";
      default:
         return "UNTYPED";
      }
   }
}
