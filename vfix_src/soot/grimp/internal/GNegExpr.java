package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractNegExpr;

public class GNegExpr extends AbstractNegExpr {
   public GNegExpr(Value op) {
      super(Grimp.v().newExprBox(op));
   }

   public Object clone() {
      return new GNegExpr(Grimp.cloneIfNecessary(this.getOp()));
   }
}
