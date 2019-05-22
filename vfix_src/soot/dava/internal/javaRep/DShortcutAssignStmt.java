package soot.dava.internal.javaRep;

import soot.Type;
import soot.UnitPrinter;

public class DShortcutAssignStmt extends DAssignStmt {
   Type type;

   public DShortcutAssignStmt(DAssignStmt assignStmt, Type type) {
      super(assignStmt.getLeftOpBox(), assignStmt.getRightOpBox());
      this.type = type;
   }

   public void toString(UnitPrinter up) {
      up.type(this.type);
      up.literal(" ");
      super.toString(up);
   }

   public String toString() {
      return this.type.toString() + " " + this.leftBox.getValue().toString() + " = " + this.rightBox.getValue().toString();
   }
}
