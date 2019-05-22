package soot.grimp.internal;

import soot.Unit;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JIfStmt;

public class GIfStmt extends JIfStmt {
   public GIfStmt(Value condition, Unit target) {
      super(Grimp.v().newConditionExprBox(condition), Grimp.v().newStmtBox(target));
   }

   public Object clone() {
      return new GIfStmt(Grimp.cloneIfNecessary(this.getCondition()), this.getTarget());
   }
}
