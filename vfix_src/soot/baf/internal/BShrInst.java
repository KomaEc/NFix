package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.ShrInst;
import soot.util.Switch;

public class BShrInst extends AbstractOpTypeInst implements ShrInst {
   public BShrInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BShrInst(this.getOpType());
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
      return "shr";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseShrInst(this);
   }
}
