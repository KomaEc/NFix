package soot.jimple;

import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;

public interface CastExpr extends Expr {
   Value getOp();

   void setOp(Value var1);

   ValueBox getOpBox();

   Type getCastType();

   void setCastType(Type var1);

   Type getType();

   void apply(Switch var1);
}
