package soot.grimp.internal;

import soot.Type;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractInstanceOfExpr;

public class GInstanceOfExpr extends AbstractInstanceOfExpr {
   public GInstanceOfExpr(Value op, Type checkType) {
      super(Grimp.v().newObjExprBox(op), checkType);
   }

   public Object clone() {
      return new GInstanceOfExpr(Grimp.cloneIfNecessary(this.getOp()), this.getCheckType());
   }
}
