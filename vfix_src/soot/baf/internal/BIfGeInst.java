package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfGeInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfGeInst extends AbstractBranchInst implements IfGeInst {
   public BIfGeInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BIfGeInst(this.getTarget());
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
      return "ifge";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfGeInst(this);
   }
}
