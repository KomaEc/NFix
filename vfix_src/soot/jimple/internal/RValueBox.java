package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Immediate;
import soot.Value;
import soot.jimple.ConcreteRef;
import soot.jimple.Expr;

public class RValueBox extends AbstractValueBox {
   public RValueBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof Immediate || value instanceof ConcreteRef || value instanceof Expr;
   }
}
