package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface ReturnStmt extends Stmt {
   ValueBox getOpBox();

   void setOp(Value var1);

   Value getOp();
}
