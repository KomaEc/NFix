package soot.baf.internal;

import soot.baf.InstSwitch;
import soot.baf.ThrowInst;
import soot.util.Switch;

public class BThrowInst extends AbstractInst implements ThrowInst {
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
      return "athrow";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseThrowInst(this);
   }

   public Object clone() {
      return new BThrowInst();
   }

   public boolean fallsThrough() {
      return false;
   }
}
