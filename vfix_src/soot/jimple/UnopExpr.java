package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface UnopExpr extends Expr {
   Value getOp();

   void setOp(Value var1);

   ValueBox getOpBox();
}
