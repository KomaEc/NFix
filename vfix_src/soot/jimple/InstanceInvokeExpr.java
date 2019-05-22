package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface InstanceInvokeExpr extends InvokeExpr {
   Value getBase();

   ValueBox getBaseBox();

   void setBase(Value var1);
}
