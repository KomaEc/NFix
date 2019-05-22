package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.ShlInst;
import soot.util.Switch;

public class BShlInst extends AbstractOpTypeInst implements ShlInst {
   public BShlInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BShlInst(this.getOpType());
   }

   public int getInMachineCount() {
      return AbstractJasminClass.sizeOfType(this.getOpType()) + 1;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public final String getName() {
      return "shl";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseShlInst(this);
   }
}
