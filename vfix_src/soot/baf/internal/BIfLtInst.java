package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfLtInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfLtInst extends AbstractBranchInst implements IfLtInst {
   public BIfLtInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BIfLtInst(this.getTarget());
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
      return "iflt";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfLtInst(this);
   }
}
