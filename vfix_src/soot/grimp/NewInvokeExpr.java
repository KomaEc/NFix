package soot.grimp;

import soot.RefType;
import soot.jimple.StaticInvokeExpr;

public interface NewInvokeExpr extends StaticInvokeExpr {
   RefType getBaseType();

   void setBaseType(RefType var1);
}
