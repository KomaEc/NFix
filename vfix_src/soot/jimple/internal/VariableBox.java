package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Local;
import soot.Value;
import soot.jimple.ConcreteRef;

public class VariableBox extends AbstractValueBox {
   public VariableBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof Local || value instanceof ConcreteRef;
   }
}
