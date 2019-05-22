package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.XorInst;
import soot.util.Switch;

public class BXorInst extends AbstractOpTypeInst implements XorInst {
   public BXorInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public int getOutCount() {
      return 1;
   }

   public Object clone() {
      return new BXorInst(this.getOpType());
   }

   public int getInMachineCount() {
      return 2 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutMachineCount() {
      return 1 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public final String getName() {
      return "xor";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseXorInst(this);
   }
}
