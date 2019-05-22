package corg.vfix.sa.ddg;

import soot.Unit;
import soot.Value;
import soot.jimple.InstanceFieldRef;

public class UnitValuePair {
   public final Unit unit;
   public final Value value;

   public UnitValuePair(Unit u, Value v) {
      this.unit = u;
      this.value = v;
   }

   public boolean equals(Object O) {
      if (!(O instanceof UnitValuePair)) {
         return false;
      } else {
         UnitValuePair key = (UnitValuePair)O;
         if (key.unit != null && key.value != null) {
            if (key.value instanceof InstanceFieldRef && this.value instanceof InstanceFieldRef) {
               InstanceFieldRef kifr = (InstanceFieldRef)key.value;
               InstanceFieldRef vifr = (InstanceFieldRef)this.value;
               return key.unit.equals(this.unit) && kifr.equivTo(vifr);
            } else {
               return key.unit.equals(this.unit) && key.value.equals(this.value);
            }
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      if (this.value instanceof InstanceFieldRef) {
         InstanceFieldRef ifr = (InstanceFieldRef)this.value;
         return this.unit.hashCode() + ifr.equivHashCode();
      } else {
         return this.unit.hashCode() + this.value.hashCode();
      }
   }

   public String toString() {
      return "{" + this.value.toString() + "@" + this.unit.toString() + "}";
   }

   public Unit getUnit() {
      return this.unit;
   }
}
