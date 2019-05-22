package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.UshrInst;
import soot.util.Switch;

public class BUshrInst extends AbstractOpTypeInst implements UshrInst {
   public BUshrInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BUshrInst(this.getOpType());
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
      return "ushr";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseUshrInst(this);
   }
}
