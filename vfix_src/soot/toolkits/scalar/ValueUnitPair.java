package soot.toolkits.scalar;

import soot.AbstractValueBox;
import soot.EquivTo;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.jimple.Jimple;

public class ValueUnitPair extends AbstractValueBox implements UnitBox, EquivTo {
   protected Unit unit;

   public ValueUnitPair(Value value, Unit unit) {
      this.setValue(value);
      this.setUnit(unit);
   }

   public boolean canContainValue(Value value) {
      return true;
   }

   public void setUnit(Unit unit) {
      if (!this.canContainUnit(unit)) {
         throw new RuntimeException("Cannot put " + unit + " in this box");
      } else {
         if (this.unit != null) {
            this.unit.removeBoxPointingToThis(this);
         }

         this.unit = unit;
         if (this.unit != null) {
            this.unit.addBoxPointingToThis(this);
         }

      }
   }

   public Unit getUnit() {
      return this.unit;
   }

   public boolean canContainUnit(Unit u) {
      return true;
   }

   public boolean isBranchTarget() {
      return true;
   }

   public String toString() {
      return "Value = " + this.getValue() + ", Unit = " + this.getUnit();
   }

   public void toString(UnitPrinter up) {
      super.toString(up);
      if (this.isBranchTarget()) {
         up.literal(", ");
      } else {
         up.literal(" #");
      }

      up.startUnitBox(this);
      up.unitRef(this.unit, this.isBranchTarget());
      up.endUnitBox(this);
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object other) {
      return super.equals(other);
   }

   public boolean equivTo(Object other) {
      return other instanceof ValueUnitPair && ((ValueUnitPair)other).getValue().equivTo(this.getValue()) && ((ValueUnitPair)other).getUnit().equals(this.getUnit());
   }

   public int equivHashCode() {
      return this.getUnit().hashCode() * 17 + this.getValue().equivHashCode() * 101;
   }

   public Object clone() {
      Value cv = Jimple.cloneIfNecessary(this.getValue());
      Unit cu = this.getUnit();
      return new ValueUnitPair(cv, cu);
   }
}
