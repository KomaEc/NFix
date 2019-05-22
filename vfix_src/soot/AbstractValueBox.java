package soot;

import soot.tagkit.AbstractHost;

public abstract class AbstractValueBox extends AbstractHost implements ValueBox {
   Value value;

   public void setValue(Value value) {
      if (value == null) {
         throw new IllegalArgumentException("value may not be null");
      } else if (this.canContainValue(value)) {
         this.value = value;
      } else {
         throw new RuntimeException("Box " + this + " cannot contain value: " + value + "(" + value.getClass() + ")");
      }
   }

   public Value getValue() {
      return this.value;
   }

   public void toString(UnitPrinter up) {
      up.startValueBox(this);
      this.value.toString(up);
      up.endValueBox(this);
   }

   public String toString() {
      return this.getClass().getSimpleName() + "(" + this.value + ")";
   }
}
