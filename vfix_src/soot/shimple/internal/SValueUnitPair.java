package soot.shimple.internal;

import soot.Unit;
import soot.Value;
import soot.toolkits.scalar.ValueUnitPair;

public class SValueUnitPair extends ValueUnitPair implements SUnitBox {
   protected boolean unitChanged = false;

   public SValueUnitPair(Value value, Unit unit) {
      super(value, unit);
      this.setUnitChanged(true);
   }

   public boolean isBranchTarget() {
      return false;
   }

   public void setUnit(Unit u) {
      super.setUnit(u);
      this.setUnitChanged(true);
   }

   public boolean isUnitChanged() {
      return this.unitChanged;
   }

   public void setUnitChanged(boolean unitChanged) {
      this.unitChanged = unitChanged;
   }
}
