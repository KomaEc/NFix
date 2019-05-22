package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfNeInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BIfNeInst extends AbstractBranchInst implements IfNeInst {
   public BIfNeInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BIfNeInst(this.getTarget());
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
      return "ifne";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseIfNeInst(this);
   }
}
