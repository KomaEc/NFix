package soot.jimple;

import soot.Type;
import soot.util.Switch;

public interface StaticInvokeExpr extends InvokeExpr {
   Type getType();

   void apply(Switch var1);
}
