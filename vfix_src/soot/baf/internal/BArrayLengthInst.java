package soot.baf.internal;

import soot.baf.ArrayLengthInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BArrayLengthInst extends AbstractInst implements ArrayLengthInst {
   public int getInCount() {
      return 1;
   }

   public int getInMachineCount() {
      return 1;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public final String getName() {
      return "arraylength";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseArrayLengthInst(this);
   }

   public Object clone() {
      return new BArrayLengthInst();
   }
}
