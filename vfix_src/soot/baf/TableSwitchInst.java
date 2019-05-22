package soot.baf;

import java.util.List;
import soot.Unit;
import soot.UnitBox;

public interface TableSwitchInst extends Inst {
   Unit getDefaultTarget();

   void setDefaultTarget(Unit var1);

   UnitBox getDefaultTargetBox();

   int getLowIndex();

   void setLowIndex(int var1);

   int getHighIndex();

   void setHighIndex(int var1);

   List<Unit> getTargets();

   Unit getTarget(int var1);

   void setTarget(int var1, Unit var2);

   void setTargets(List<Unit> var1);

   UnitBox getTargetBox(int var1);
}
