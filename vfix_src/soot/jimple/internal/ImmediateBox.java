package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Immediate;
import soot.Value;

public class ImmediateBox extends AbstractValueBox {
   public ImmediateBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof Immediate;
   }
}
