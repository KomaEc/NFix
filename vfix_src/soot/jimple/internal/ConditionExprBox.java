package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Value;
import soot.jimple.ConditionExpr;

public class ConditionExprBox extends AbstractValueBox {
   public ConditionExprBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof ConditionExpr;
   }
}
