package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.MulInst;
import soot.util.Switch;

public class BMulInst extends AbstractOpTypeInst implements MulInst {
   public BMulInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BMulInst(this.getOpType());
   }

   public int getInMachineCount() {
      return 2 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public final String getName() {
      return "mul";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseMulInst(this);
   }
}
