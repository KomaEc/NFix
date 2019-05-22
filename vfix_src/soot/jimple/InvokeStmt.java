package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface InvokeStmt extends Stmt {
   void setInvokeExpr(Value var1);

   InvokeExpr getInvokeExpr();

   ValueBox getInvokeExprBox();
}
