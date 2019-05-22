package soot.jimple;

import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;

public interface SwitchStmt extends Stmt {
   Unit getDefaultTarget();

   void setDefaultTarget(Unit var1);

   UnitBox getDefaultTargetBox();

   Value getKey();

   void setKey(Value var1);

   ValueBox getKeyBox();

   List<Unit> getTargets();

   Unit getTarget(int var1);

   void setTarget(int var1, Unit var2);

   UnitBox getTargetBox(int var1);
}
