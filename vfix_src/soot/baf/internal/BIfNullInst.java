package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfNullInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfNullInst extends AbstractBranchInst implements IfNullInst {
   public BIfNullInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 1;
   }

   public int getOutCount() {
      return 0;
   }

   public Object clone() {
      return new BIfNullInst(this.getTarget());
   }

   public int getInMachineCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public String getName() {
      return "ifnull";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfNullInst(this);
   }
}
