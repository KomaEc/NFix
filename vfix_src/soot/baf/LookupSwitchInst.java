package soot.baf;

import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.IntConstant;

public interface LookupSwitchInst extends Inst {
   Unit getDefaultTarget();

   void setDefaultTarget(Unit var1);

   UnitBox getDefaultTargetBox();

   void setLookupValue(int var1, int var2);

   int getLookupValue(int var1);

   List<IntConstant> getLookupValues();

   void setLookupValues(List<IntConstant> var1);

   int getTargetCount();

   Unit getTarget(int var1);

   UnitBox getTargetBox(int var1);

   void setTarget(int var1, Unit var2);

   List<Unit> getTargets();

   void setTargets(List<Unit> var1);
}
