package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JInvokeStmt;

public class GInvokeStmt extends JInvokeStmt {
   public GInvokeStmt(Value c) {
      super(Grimp.v().newInvokeExprBox(c));
   }

   public Object clone() {
      return new GInvokeStmt(Grimp.cloneIfNecessary(this.getInvokeExpr()));
   }
}
