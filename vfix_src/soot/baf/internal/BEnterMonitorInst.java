package soot.baf.internal;

import soot.baf.EnterMonitorInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BEnterMonitorInst extends AbstractInst implements EnterMonitorInst {
   public int getInCount() {
      return 1;
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

   public final String getName() {
      return "entermonitor";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseEnterMonitorInst(this);
   }

   public Object clone() {
      return new BEnterMonitorInst();
   }
}
