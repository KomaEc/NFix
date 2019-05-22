package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.CmpgInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BCmpgInst extends AbstractOpTypeInst implements CmpgInst {
   public BCmpgInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BCmpgInst(this.getOpType());
   }

   public int getInMachineCount() {
      return 2 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public final String getName() {
      return "cmpg";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseCmpgInst(this);
   }
}
