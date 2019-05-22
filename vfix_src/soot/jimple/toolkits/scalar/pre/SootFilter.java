package soot.jimple.toolkits.scalar.pre;

import soot.EquivalentValue;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConcreteRef;
import soot.jimple.Constant;
import soot.jimple.DivExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.RemExpr;
import soot.jimple.UnopExpr;

public class SootFilter {
   public static EquivalentValue equiVal(Value val) {
      return val == null ? null : new EquivalentValue(val);
   }

   public static Value rhs(Unit unit) {
      return unit instanceof AssignStmt ? ((AssignStmt)unit).getRightOp() : null;
   }

   public static Value binop(Value val) {
      if (val == null) {
         return null;
      } else {
         return val instanceof BinopExpr ? val : null;
      }
   }

   public static Value binopRhs(Unit unit) {
      return binop(rhs(unit));
   }

   public static Value concreteRef(Value val) {
      if (val == null) {
         return null;
      } else {
         return val instanceof ConcreteRef ? val : null;
      }
   }

   public static Value noExceptionThrowing(Value val) {
      if (val == null) {
         return null;
      } else {
         return !throwsException(val) ? val : null;
      }
   }

   public static Value noExceptionThrowingRhs(Unit unit) {
      return noExceptionThrowing(rhs(unit));
   }

   public static Value noInvokeRhs(Unit unit) {
      return noInvoke(rhs(unit));
   }

   public static Value noInvoke(Value val) {
      return val != null && !isInvoke(val) ? val : null;
   }

   public static boolean isInvoke(Value val) {
      val = getEquivalentValueRoot(val);
      return val instanceof InvokeExpr;
   }

   public static Value local(Value val) {
      return val != null && isLocal(val) ? val : null;
   }

   public static Value noLocal(Value val) {
      return val != null && !isLocal(val) ? val : null;
   }

   public static boolean isLocal(Value val) {
      return getEquivalentValueRoot(val) instanceof Local;
   }

   public static Value getEquivalentValueRoot(Value val) {
      if (val == null) {
         return null;
      } else {
         while(val instanceof EquivalentValue) {
            val = ((EquivalentValue)val).getValue();
         }

         return val;
      }
   }

   public static boolean throwsException(Value val) {
      val = getEquivalentValueRoot(val);
      if (!(val instanceof BinopExpr) && !(val instanceof UnopExpr) && !(val instanceof Local) && !(val instanceof Constant)) {
         return true;
      } else {
         return val instanceof DivExpr || val instanceof RemExpr || val instanceof LengthExpr;
      }
   }
}
