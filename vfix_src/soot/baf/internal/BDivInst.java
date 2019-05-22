package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.DivInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BDivInst extends AbstractOpTypeInst implements DivInst {
   public BDivInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BDivInst(this.getOpType());
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
      return "div";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseDivInst(this);
   }
}
