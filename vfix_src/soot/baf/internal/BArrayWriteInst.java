package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.ArrayWriteInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BArrayWriteInst extends AbstractOpTypeInst implements ArrayWriteInst {
   public BArrayWriteInst(Type opType) {
      super(opType);
   }

   public int getInCount() {
      return 3;
   }

   public Object clone() {
      return new BArrayWriteInst(this.getOpType());
   }

   public int getInMachineCount() {
      return 2 + AbstractJasminClass.sizeOfType(this.getOpType());
   }

   public int getOutCount() {
      return 0;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public final String getName() {
      return "arraywrite";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseArrayWriteInst(this);
   }

   public boolean containsArrayRef() {
      return true;
   }
}
