package soot.jimple;

import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;

public interface IfStmt extends Stmt {
   Value getCondition();

   void setCondition(Value var1);

   ValueBox getConditionBox();

   Stmt getTarget();

   void setTarget(Unit var1);

   UnitBox getTargetBox();
}
