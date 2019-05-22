package soot.toolkits.scalar;

import soot.Local;
import soot.Unit;

public class LocalUnitPair {
   Local local;
   Unit unit;

   public LocalUnitPair(Local local, Unit unit) {
      this.local = local;
      this.unit = unit;
   }

   public boolean equals(Object other) {
      return other instanceof LocalUnitPair && ((LocalUnitPair)other).local == this.local && ((LocalUnitPair)other).unit == this.unit;
   }

   public int hashCode() {
      return this.local.hashCode() * 101 + this.unit.hashCode() + 17;
   }

   public Local getLocal() {
      return this.local;
   }

   public Unit getUnit() {
      return this.unit;
   }
}
