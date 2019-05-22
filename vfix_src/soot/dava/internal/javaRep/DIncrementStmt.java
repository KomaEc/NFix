package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.internal.GAssignStmt;

public class DIncrementStmt extends GAssignStmt {
   public DIncrementStmt(Value variable, Value rvalue) {
      super(variable, rvalue);
   }

   public Object clone() {
      return new DIncrementStmt(Grimp.cloneIfNecessary(this.getLeftOp()), Grimp.cloneIfNecessary(this.getRightOp()));
   }

   public String toString() {
      return this.getLeftOpBox().getValue().toString() + "++";
   }

   public void toString(UnitPrinter up) {
      this.getLeftOpBox().toString(up);
      up.literal("++");
   }
}
