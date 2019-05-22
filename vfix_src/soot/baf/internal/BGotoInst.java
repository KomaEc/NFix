package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.GotoInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BGotoInst extends AbstractBranchInst implements GotoInst {
   public BGotoInst(Unit target) {
      super(Baf.v().newInstBox(target));
   }

   public Object clone() {
      return new BGotoInst(this.getTarget());
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
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public String getName() {
      return "goto";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseGotoInst(this);
   }

   public boolean fallsThrough() {
      return false;
   }
}
