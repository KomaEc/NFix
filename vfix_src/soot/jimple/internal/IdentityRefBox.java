package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Value;
import soot.jimple.IdentityRef;

public class IdentityRefBox extends AbstractValueBox {
   public IdentityRefBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof IdentityRef;
   }
}
