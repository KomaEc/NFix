package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.ReturnInst;
import soot.util.Switch;

public class BReturnInst extends AbstractOpTypeInst implements ReturnInst {
   public BReturnInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BReturnInst(this.getOpType());
   }

   public int getInMachineCount() {
      return AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public final String getName() {
      return "return";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseReturnInst(this);
   }

   public boolean fallsThrough() {
      return false;
   }
}
