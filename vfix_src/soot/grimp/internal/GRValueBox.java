package soot.grimp.internal;

import soot.AbstractValueBox;
import soot.Local;
import soot.Value;
import soot.jimple.ConcreteRef;
import soot.jimple.Constant;
import soot.jimple.Expr;

public class GRValueBox extends AbstractValueBox {
   public GRValueBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof Local || value instanceof Constant || value instanceof ConcreteRef || value instanceof Expr;
   }

   public Object clone() {
      throw new RuntimeException();
   }
}
