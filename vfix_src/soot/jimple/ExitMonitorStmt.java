package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface ExitMonitorStmt extends MonitorStmt {
   Value getOp();

   void setOp(Value var1);

   ValueBox getOpBox();
}
