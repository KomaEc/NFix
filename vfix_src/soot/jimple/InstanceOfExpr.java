package soot.jimple;

import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;

public interface InstanceOfExpr extends Expr {
   Value getOp();

   void setOp(Value var1);

   ValueBox getOpBox();

   Type getType();

   Type getCheckType();

   void setCheckType(Type var1);

   void apply(Switch var1);
}
