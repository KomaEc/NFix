package soot.grimp.internal;

import soot.AbstractValueBox;
import soot.Local;
import soot.Value;
import soot.jimple.ConcreteRef;
import soot.jimple.Constant;
import soot.jimple.Expr;

public class ExprBox extends AbstractValueBox {
   public ExprBox(Value value) {
      this.setValue(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof Local || value instanceof Constant || value instanceof Expr || value instanceof ConcreteRef;
   }
}
