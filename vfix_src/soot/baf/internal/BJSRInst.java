package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.InstSwitch;
import soot.baf.JSRInst;
import soot.util.Switch;

public class BJSRInst extends AbstractBranchInst implements JSRInst {
   public BJSRInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public Object clone() {
      return new BJSRInst(this.getTarget());
   }

   public int getInMachineCount() {
      return 0;
   }

   public boolean branches() {
      return true;
   }

   public int getInCount() {
      return 0;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public String getName() {
      return "jsr";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseJSRInst(this);
   }

   public boolean fallsThrough() {
      return false;
   }
}
