package soot.baf.internal;

import soot.baf.InstSwitch;
import soot.baf.ReturnVoidInst;
import soot.util.Switch;

public class BReturnVoidInst extends AbstractInst implements ReturnVoidInst {
   public int getInCount() {
      return 0;
   }

   public Object clone() {
      return new BReturnVoidInst();
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
      return "return";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseReturnVoidInst(this);
   }

   public boolean fallsThrough() {
      return false;
   }
}
