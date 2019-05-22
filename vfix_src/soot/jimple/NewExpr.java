package soot.jimple;

import soot.RefType;
import soot.Type;
import soot.util.Switch;

public interface NewExpr extends Expr, AnyNewExpr {
   RefType getBaseType();

   void setBaseType(RefType var1);

   Type getType();

   void apply(Switch var1);
}
