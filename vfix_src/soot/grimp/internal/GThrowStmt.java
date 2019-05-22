package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JThrowStmt;

public class GThrowStmt extends JThrowStmt {
   public GThrowStmt(Value op) {
      super(Grimp.v().newExprBox(op));
   }

   public Object clone() {
      return new GThrowStmt(Grimp.cloneIfNecessary(this.getOp()));
   }
}
