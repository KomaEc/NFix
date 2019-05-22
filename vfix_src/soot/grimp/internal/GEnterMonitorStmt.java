package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JEnterMonitorStmt;

public class GEnterMonitorStmt extends JEnterMonitorStmt {
   public GEnterMonitorStmt(Value op) {
      super(Grimp.v().newExprBox(op));
   }

   public Object clone() {
      return new GEnterMonitorStmt(Grimp.cloneIfNecessary(this.getOp()));
   }
}
