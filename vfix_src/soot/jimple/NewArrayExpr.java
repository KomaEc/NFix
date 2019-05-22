package soot.jimple;

import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;

public interface NewArrayExpr extends Expr, AnyNewExpr {
   Type getBaseType();

   void setBaseType(Type var1);

   ValueBox getSizeBox();

   Value getSize();

   void setSize(Value var1);

   Type getType();

   void apply(Switch var1);
}
