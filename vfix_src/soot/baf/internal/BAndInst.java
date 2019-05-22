package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.AndInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BAndInst extends AbstractOpTypeInst implements AndInst {
   public BAndInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BAndInst(this.getOpType());
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
      return "and";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseAndInst(this);
   }
}
