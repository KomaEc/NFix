package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface ThrowStmt extends Stmt {
   ValueBox getOpBox();

   Value getOp();

   void setOp(Value var1);
}
