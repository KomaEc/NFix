package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JReturnStmt;

public class GReturnStmt extends JReturnStmt {
   public GReturnStmt(Value returnValue) {
      super(Grimp.v().newExprBox(returnValue));
   }

   public Object clone() {
      return new GReturnStmt(Grimp.cloneIfNecessary(this.getOp()));
   }
}
