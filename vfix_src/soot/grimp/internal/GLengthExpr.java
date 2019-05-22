package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractLengthExpr;

public class GLengthExpr extends AbstractLengthExpr {
   public GLengthExpr(Value op) {
      super(Grimp.v().newObjExprBox(op));
   }

   public Object clone() {
      return new GLengthExpr(Grimp.cloneIfNecessary(this.getOp()));
   }
}
