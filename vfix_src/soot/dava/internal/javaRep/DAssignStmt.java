package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.internal.AbstractDefinitionStmt;

public class DAssignStmt extends AbstractDefinitionStmt implements AssignStmt {
   public DAssignStmt(ValueBox left, ValueBox right) {
      super(left, right);
   }

   public Object clone() {
      return new DAssignStmt(this.leftBox, this.rightBox);
   }

   public void setLeftOp(Value variable) {
      this.leftBox.setValue(variable);
   }

   public void setRightOp(Value rvalue) {
      this.rightBox.setValue(rvalue);
   }

   public void toString(UnitPrinter up) {
      this.leftBox.toString(up);
      up.literal(" = ");
      this.rightBox.toString(up);
   }

   public String toString() {
      return this.leftBox.getValue().toString() + " = " + this.rightBox.getValue().toString();
   }
}
