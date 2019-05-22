package soot.jimple;

import soot.Value;

public interface AssignStmt extends DefinitionStmt {
   void setLeftOp(Value var1);

   void setRightOp(Value var1);
}
