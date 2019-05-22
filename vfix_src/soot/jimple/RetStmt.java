package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface RetStmt extends Stmt {
   Value getStmtAddress();

   ValueBox getStmtAddressBox();

   void setStmtAddress(Value var1);
}
