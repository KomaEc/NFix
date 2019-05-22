package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.NegInst;
import soot.util.Switch;

public class BNegInst extends AbstractOpTypeInst implements NegInst {
   public BNegInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 1;
   }

   public int getOutCount() {
      return 1;
   }

   public Object clone() {
      return new BNegInst(this.getOpType());
   }

   public int getInMachineCount() {
      return AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutMachineCount() {
      return AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public final String getName() {
      return "neg";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseNegInst(this);
   }
}
