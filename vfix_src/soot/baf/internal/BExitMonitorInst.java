package soot.baf.internal;

import soot.baf.ExitMonitorInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BExitMonitorInst extends AbstractInst implements ExitMonitorInst {
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
      return "exitmonitor";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseExitMonitorInst(this);
   }

   public Object clone() {
      return new BExitMonitorInst();
   }
}
