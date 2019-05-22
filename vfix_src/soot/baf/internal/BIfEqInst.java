package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfEqInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfEqInst extends AbstractBranchInst implements IfEqInst {
   public BIfEqInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BIfEqInst(this.getTarget());
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
      return "ifeq";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfEqInst(this);
   }
}
