package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JExitMonitorStmt;

public class GExitMonitorStmt extends JExitMonitorStmt {
   public GExitMonitorStmt(Value op) {
      super(Grimp.v().newExprBox(op));
   }

   public Object clone() {
      return new GExitMonitorStmt(Grimp.cloneIfNecessary(this.getOp()));
   }
}
