package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfGtInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfGtInst extends AbstractBranchInst implements IfGtInst {
   public BIfGtInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BIfGtInst(this.getTarget());
   }

   public int getInMachineCount() {
      return 1;
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public String getName() {
      return "ifgt";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfGtInst(this);
   }
}
