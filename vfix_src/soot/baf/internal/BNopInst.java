package soot.baf.internal;

import soot.baf.InstSwitch;
import soot.baf.NopInst;
import soot.util.Switch;

public class BNopInst extends AbstractInst implements NopInst {
   public int getInCount() {
      return 0;
   }

   public Object clone() {
      return new BNopInst();
   }

   public int getInMachineCount() {
      return 0;
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public final String getName() {
      return "nop";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseNopInst(this);
   }
}
