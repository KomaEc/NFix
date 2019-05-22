package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.SubInst;
import soot.util.Switch;

public class BSubInst extends AbstractOpTypeInst implements SubInst {
   public BSubInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public int getInMachineCount() {
      return 2 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public Object clone() {
      return new BSubInst(this.getOpType());
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public final String getName() {
      return "sub";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseSubInst(this);
   }
}
