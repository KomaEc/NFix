package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface MonitorStmt extends Stmt {
   Value getOp();

   void setOp(Value var1);

   ValueBox getOpBox();
}
