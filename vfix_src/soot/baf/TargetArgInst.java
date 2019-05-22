package soot.baf;

import soot.Unit;
import soot.UnitBox;

public interface TargetArgInst extends Inst {
   Unit getTarget();

   UnitBox getTargetBox();

   void setTarget(Unit var1);
}
