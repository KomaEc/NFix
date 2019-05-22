package soot.toolkits.exceptions;

import soot.AnySubType;
import soot.NullType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.baf.ThrowInst;
import soot.grimp.NewInvokeExpr;
import soot.jimple.ThrowStmt;

public abstract class AbstractThrowAnalysis implements ThrowAnalysis {
   public abstract ThrowableSet mightThrow(Unit var1);

   public ThrowableSet mightThrowExplicitly(ThrowInst t) {
      return ThrowableSet.Manager.v().ALL_THROWABLES;
   }

   public ThrowableSet mightThrowExplicitly(ThrowStmt t) {
      Value thrownExpression = t.getOp();
      Type thrownType = thrownExpression.getType();
      if (thrownType != null && !(thrownType instanceof UnknownType)) {
         ThrowableSet result;
         if (thrownType instanceof NullType) {
            result = ThrowableSet.Manager.v().EMPTY;
            result = result.add(ThrowableSet.Manager.v().NULL_POINTER_EXCEPTION);
            return result;
         } else if (!(thrownType instanceof RefType)) {
            throw new IllegalStateException("UnitThrowAnalysis StmtSwitch: type of throw argument is not a RefType!");
         } else {
            result = ThrowableSet.Manager.v().EMPTY;
            if (thrownExpression instanceof NewInvokeExpr) {
               result = result.add((RefType)thrownType);
            } else {
               result = result.add(AnySubType.v((RefType)thrownType));
            }

            return result;
         }
      } else {
         return ThrowableSet.Manager.v().ALL_THROWABLES;
      }
   }

   public abstract ThrowableSet mightThrowImplicitly(ThrowInst var1);

   public abstract ThrowableSet mightThrowImplicitly(ThrowStmt var1);
}
