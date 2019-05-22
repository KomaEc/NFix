package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.AddInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BAddInst extends AbstractOpTypeInst implements AddInst {
   public BAddInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BAddInst(this.getOpType());
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
      return "add";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseAddInst(this);
   }
}
