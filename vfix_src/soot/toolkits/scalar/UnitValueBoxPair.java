package soot.toolkits.scalar;

import soot.Unit;
import soot.ValueBox;

public class UnitValueBoxPair {
   public Unit unit;
   public ValueBox valueBox;

   public UnitValueBoxPair(Unit unit, ValueBox valueBox) {
      this.unit = unit;
      this.valueBox = valueBox;
   }

   public boolean equals(Object other) {
      if (other instanceof UnitValueBoxPair) {
         UnitValueBoxPair otherPair = (UnitValueBoxPair)other;
         if (this.unit.equals(otherPair.unit) && this.valueBox.equals(otherPair.valueBox)) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      return this.unit.hashCode() + this.valueBox.hashCode();
   }

   public String toString() {
      return this.valueBox + " in " + this.unit;
   }

   public Unit getUnit() {
      return this.unit;
   }

   public ValueBox getValueBox() {
      return this.valueBox;
   }
}
