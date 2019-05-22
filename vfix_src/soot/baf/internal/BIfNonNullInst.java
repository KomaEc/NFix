package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfNonNullInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfNonNullInst extends AbstractBranchInst implements IfNonNullInst {
   public BIfNonNullInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BIfNonNullInst(this.getTarget());
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
      return "ifnonnull";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfNonNullInst(this);
   }
}
