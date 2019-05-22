package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfLeInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfLeInst extends AbstractBranchInst implements IfLeInst {
   public BIfLeInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BIfLeInst(this.getTarget());
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
      return "ifle";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfLeInst(this);
   }
}
