package soot.baf.internal;

import soot.AbstractValueBox;
import soot.Value;

public class BafLocalBox extends AbstractValueBox {
   public BafLocalBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof BafLocal;
   }
}
