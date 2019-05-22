package soot.jimple;

import soot.Unit;
import soot.UnitBox;

public interface GotoStmt extends Stmt {
   Unit getTarget();

   void setTarget(Unit var1);

   UnitBox getTargetBox();
}
