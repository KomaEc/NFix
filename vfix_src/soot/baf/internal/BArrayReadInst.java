package soot.baf.internal;

import soot.Type;
import soot.baf.ArrayReadInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BArrayReadInst extends AbstractOpTypeInst implements ArrayReadInst {
   public BArrayReadInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BArrayReadInst(this.getOpType());
   }

   public int getInMachineCount() {
      return 2;
   }

   public int getOutCount() {
      return 1;
   }

   public final String getName() {
      return "arrayread";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseArrayReadInst(this);
   }

   public boolean containsArrayRef() {
      return true;
   }
}
