package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Value;

public class JimpleLocalBox extends AbstractValueBox {
   public JimpleLocalBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof JimpleLocal;
   }
}
