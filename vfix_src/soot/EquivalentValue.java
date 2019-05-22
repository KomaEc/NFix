package soot;

import java.util.List;
import soot.util.Switch;

public class EquivalentValue implements Value {
   Value e;

   public EquivalentValue(Value e) {
      if (e instanceof EquivalentValue) {
         e = ((EquivalentValue)e).e;
      }

      this.e = e;
   }

   public boolean equals(Object o) {
      if (o instanceof EquivalentValue) {
         o = ((EquivalentValue)o).e;
      }

      return this.e.equivTo(o);
   }

   public boolean equivToValue(Value v) {
      return this.e.equivTo(v);
   }

   public boolean equalsToValue(Value v) {
      return this.e.equals(v);
   }

   /** @deprecated */
   @Deprecated
   public Value getDeepestValue() {
      return this.getValue();
   }

   public int hashCode() {
      return this.e.equivHashCode();
   }

   public String toString() {
      return this.e.toString();
   }

   public Value getValue() {
      return this.e;
   }

   public List<ValueBox> getUseBoxes() {
      return this.e.getUseBoxes();
   }

   public Type getType() {
      return this.e.getType();
   }

   public Object clone() {
      EquivalentValue equiVal = new EquivalentValue((Value)this.e.clone());
      return equiVal;
   }

   public boolean equivTo(Object o) {
      return this.e.equivTo(o);
   }

   public int equivHashCode() {
      return this.e.equivHashCode();
   }

   public void apply(Switch sw) {
      this.e.apply(sw);
   }

   public void toString(UnitPrinter up) {
      this.e.toString(up);
   }
}
