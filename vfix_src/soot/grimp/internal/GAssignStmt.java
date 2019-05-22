package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JAssignStmt;

public class GAssignStmt extends JAssignStmt {
   public GAssignStmt(Value variable, Value rvalue) {
      super(Grimp.v().newVariableBox(variable), Grimp.v().newRValueBox(rvalue));
   }

   public Object clone() {
      return new GAssignStmt(Grimp.cloneIfNecessary(this.getLeftOp()), Grimp.cloneIfNecessary(this.getRightOp()));
   }
}
