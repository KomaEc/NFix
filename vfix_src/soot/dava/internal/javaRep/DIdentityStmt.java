package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.internal.GIdentityStmt;

public class DIdentityStmt extends GIdentityStmt {
   public DIdentityStmt(Value local, Value identityValue) {
      super(local, identityValue);
   }

   public void toString(UnitPrinter up) {
      this.getLeftOpBox().toString(up);
      up.literal(" := ");
      this.getRightOpBox().toString(up);
   }

   public String toString() {
      return this.getLeftOpBox().getValue().toString() + " = " + this.getRightOpBox().getValue().toString();
   }
}
