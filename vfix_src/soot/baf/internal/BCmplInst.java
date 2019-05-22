package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.CmplInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BCmplInst extends AbstractOpTypeInst implements CmplInst {
   public BCmplInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 2;
   }

   public Object clone() {
      return new BCmplInst(this.getOpType());
   }

   public int getInMachineCount() {
      return 2 * AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public final String getName() {
      return "cmpl";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseCmplInst(this);
   }
}
