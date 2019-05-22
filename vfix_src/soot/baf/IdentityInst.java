package soot.baf;

import soot.IdentityUnit;
import soot.Value;
import soot.ValueBox;

public interface IdentityInst extends Inst, IdentityUnit {
   Value getLeftOp();

   Value getRightOp();

   void setLeftOp(Value var1);

   void setRightOp(Value var1);

   ValueBox getLeftOpBox();

   ValueBox getRightOpBox();
}
