package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Value;
import soot.jimple.InvokeExpr;

public class InvokeExprBox extends AbstractValueBox {
   public InvokeExprBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof InvokeExpr;
   }
}
